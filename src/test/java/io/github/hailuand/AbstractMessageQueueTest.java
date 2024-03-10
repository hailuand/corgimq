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

package io.github.hailuand;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.hailuand.corgi.mq.MessageQueue;
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;

public abstract class AbstractMessageQueueTest extends DbmsTest {
    private static final Faker faker = new Faker();
    protected MessageQueue messageQueue;
    protected String queueName;

    @Override
    protected void configure(DataSource dataSource) throws SQLException {
        super.configure(dataSource);
        this.queueName = "TestQueue_%s".formatted(faker.random().hex());
        this.mqConfig = MessageQueueConfig.of(queueName);
        this.messageQueue = MessageQueue.of(this.mqConfig);
        try (var conn = this.getConnection()) {
            this.messageQueue.createTableWithSchemaIfNotExists(conn);
        }
    }

    protected void assertTableRowCount(int expectedRowCount) throws SQLException {
        try (var conn = this.getConnection();
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
        try (var conn = this.getConnection();
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

    protected void sleepJitterForTransactionPropagation(DataSource dataSource) {
        switch (dataSource) {
            case COCKROACHDB -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
