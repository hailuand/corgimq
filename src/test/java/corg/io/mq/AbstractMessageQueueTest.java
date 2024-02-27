/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package corg.io.mq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import corg.io.mq.model.config.DatabaseConfig;
import corg.io.mq.model.config.MessageQueueConfig;
import corg.io.mq.model.message.Message;
import corg.io.mq.table.MessageQueue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;

@SuppressWarnings("SqlSourceToSinkFlow")
public abstract class AbstractMessageQueueTest {
    protected static final String QUEUE_NAME = "Test_Queue";
    protected static final String SCHEMA_NAME = "cmq";
    private static final Faker faker = new Faker();
    private static final String H2_JDBC_URL =
            "jdbc:h2:mem:TEST_DB;DATABASE_TO_UPPER=false;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER_NAME = "sa";
    private static final String H2_PASSWORD = "";

    private JdbcDatabaseContainer<?> jdbcContainer;

    protected DatabaseConfig dbConfig;
    protected MessageQueueConfig mqConfig;
    protected MessageQueue messageQueue;

    protected void configure(DataSource dataSource) throws SQLException {
        switch (dataSource) {
            case POSTGRES -> {
                assumeTrue(DockerClientFactory.instance().isDockerAvailable());
                this.jdbcContainer = RelationalTestUtil.postgresContainer();
            }
            case MYSQL -> {
                assumeTrue(DockerClientFactory.instance().isDockerAvailable());
                this.jdbcContainer = RelationalTestUtil.mySQLContainer();
            }
        }
        if (this.jdbcContainer != null && !jdbcContainer.isRunning()) {
            this.jdbcContainer.start();
        }
        this.dbConfig = DatabaseConfig.of(
                this.getJdbcUrl(dataSource), this.getUserName(dataSource), this.getPassword(dataSource));
        this.mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        this.messageQueue = MessageQueue.of(this.dbConfig, this.mqConfig);
        this.messageQueue.initialize();
    }

    protected void tearDown(DataSource dataSource) throws SQLException {
        try (var conn = this.messageQueue.getConnection();
                var st = conn.createStatement()) {
            switch (dataSource) {
                case H2 -> st.execute("DROP ALL OBJECTS");
                case POSTGRES -> st.execute("DROP SCHEMA %s CASCADE".formatted(SCHEMA_NAME));
                case MYSQL -> st.execute("DROP SCHEMA %s".formatted(SCHEMA_NAME));
            }
        }
    }

    protected String getUserName(DataSource dataSource) {
        return switch (dataSource) {
            case H2 -> H2_USER_NAME;
            case MYSQL, POSTGRES -> this.jdbcContainer.getUsername();
        };
    }

    protected String getPassword(DataSource dataSource) {
        return switch (dataSource) {
            case H2 -> H2_PASSWORD;
            case MYSQL, POSTGRES -> this.jdbcContainer.getPassword();
        };
    }

    protected String getJdbcUrl(DataSource dataSource) {
        return switch (dataSource) {
            case H2 -> H2_JDBC_URL;
            case MYSQL -> "%s?sessionVariables=sql_mode=ANSI_QUOTES".formatted(this.jdbcContainer.getJdbcUrl());
            case POSTGRES -> this.jdbcContainer.getJdbcUrl();
        };
    }

    protected void assertUniquePrimaryKeyViolation(DataSource dataSource, SQLException exception) {
        switch (dataSource) {
            case H2 -> RelationalTestUtil.assertH2PrimaryKeyViolation(exception);
            case MYSQL -> RelationalTestUtil.assertMySQLPrimaryKeyViolation(exception);
            case POSTGRES -> RelationalTestUtil.assertPostgresPrimaryKeyViolation(exception);
        }
    }

    protected void assertTableRowCount(int expectedRowCount) throws SQLException {
        try (var conn = messageQueue.getConnection();
                var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT COUNT(*) from \"%s\".\"%s\""
                    .formatted(messageQueue.tableSchemaName(), messageQueue.queueTableName()));
            Assertions.assertTrue(rs.isBeforeFirst());
            if (rs.next()) {
                var count = rs.getLong(1);
                Assertions.assertEquals(expectedRowCount, count, "Row count matches");
            }
        }
    }

    protected List<Message> createMessages(int num) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            messages.add(createMessage());
        }
        return messages;
    }

    protected Message createMessage() {
        var data =
                """
                {
                    "key": "%s",
                    "value": "%s"
                }
                """
                        .formatted(
                                faker.onePiece().character(), faker.onePiece().akumasNoMi());
        return Message.of(data);
    }

    protected void createSecondaryTable(String secondaryTableName) throws SQLException {
        try (var conn = this.messageQueue.getConnection();
                var st = conn.createStatement()) {
            var ddl =
                    """
                    CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "id" INTEGER PRIMARY KEY,
                    "some_data" TEXT NOT NULL
                    );
                    """
                            .formatted(messageQueue.tableSchemaName(), secondaryTableName);
            st.execute(ddl);
        }
    }

    protected void assertMessages(List<Message> expected, List<Message> actual) {
        assertMessages(expected, actual, null);
    }

    protected void assertMessages(List<Message> expected, List<Message> actual, String assertionMessage) {
        String providedMessage = assertionMessage == null ? "expected and actual messages equal" : assertionMessage;
        assertEquals(expected.size(), actual.size());
        var actualMap = actual.stream().collect(Collectors.toMap(Message::id, Function.identity()));
        for (var message : expected) {
            var actualMessage = actualMap.get(message.id());
            assertEquals(message, actualMessage, providedMessage);
        }
    }

    public enum DataSource {
        H2,
        MYSQL,
        POSTGRES,
    }
}
