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

package io.github.hailuand.corgi.mq;

import static org.junit.jupiter.api.Assertions.*;

import io.github.hailuand.AbstractMessageQueueTest;
import io.github.hailuand.corgi.mq.model.message.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MessageQueueTest extends AbstractMessageQueueTest {

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testInitSources(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(0);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPop(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(0);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        push(dataSource, messages);
        assertTableRowCount(messages.size());
        try (var conn = this.getConnection()) {
            var pending = this.messageQueue.read(10, conn);
            assertMessages(messages, pending);
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPopAndRead(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        push(dataSource, messages);
        assertTableRowCount(messages.size());
        try (var conn = this.getConnection()) {
            var pending = this.messageQueue.read(10, conn);
            assertMessages(messages, pending);
        }
        // Pop in another txn
        pop(dataSource, messages);
        try (var conn = this.getConnection()) {
            var pendingPostPop = this.messageQueue.read(10, conn);
            Assertions.assertTrue(pendingPostPop.isEmpty());
        }
        // assert in separate txn
        try (var conn = this.getConnection();
                var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT * from \"%s\".\"%s\" ORDER BY \"message_time\" ASC"
                    .formatted(this.messageQueue.tableSchemaName(), this.messageQueue.queueTableName()));
            Assertions.assertTrue(rs.isBeforeFirst());
            List<Message> processed = new ArrayList<>();
            while (rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                processed.add(Message.builder().id(id).data(data).build());
            }
            var messagesMap = messages.stream().collect(Collectors.toMap(Message::id, Function.identity()));
            for (var processedMessage : processed) {
                Assertions.assertTrue(messagesMap.containsKey(processedMessage.id()));
                var unprocessedMessage = messagesMap.get(processedMessage.id());
                assertEquals(unprocessedMessage, processedMessage);
            }
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testWriteDupes(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = List.of(createMessage(), createMessage());
        push(dataSource, messages);
        try (var conn = this.getConnection()) {
            assertThrows(SQLException.class, () -> {
                try {
                    this.messageQueue.push(messages, conn);
                } catch (SQLException e) {
                    assertUniquePrimaryKeyViolation(dataSource, e);
                    throw e;
                }
            });
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushNothing(DataSource dataSource) throws SQLException {
        configure(dataSource);
        try (var conn = this.getConnection()) {
            Assertions.assertTrue(this.messageQueue.read(10, conn).isEmpty());
        }
        push(dataSource, Collections.emptyList());
        try (var conn = this.getConnection()) {
            Assertions.assertTrue(this.messageQueue.read(10, conn).isEmpty());
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushTransaction(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "varieties";
        createSecondaryTable(dataSource, secondaryTableName);
        var messages = List.of(createMessage(), createMessage());
        new TransactionManager()
                .executeInTransaction(
                        () -> {
                            try {
                                return this.getConnection();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        conn -> {
                            try (var st = conn.createStatement()) {
                                // work on a different table as part of txn
                                var insertDml = """
                    INSERT INTO "%s"."%s" VALUES
                    (1, 'pembroke welsh'),
                    (2, 'cardigan welsh')
                    """.formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
                                st.executeUpdate(insertDml);
                                this.messageQueue.push(messages, conn);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
        sleepJitterForTransactionPropagation(dataSource);
        try (var conn = this.getConnection()) {
            assertMessages(messages, this.messageQueue.read(10, conn));
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushTransactionFails(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "varieties";
        createSecondaryTable(dataSource, secondaryTableName);
        assertThrows(RuntimeException.class, () -> new TransactionManager()
                .executeInTransaction(
                        () -> {
                            try {
                                return this.getConnection();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        conn -> {
                            try (var st = conn.createStatement()) {
                                // work on a different table as part of txn
                                var insertDml = """
                    INSERT INTO "%s"."%s" VALUES
                    (1, 'pembroke welsh'),
                    (2, 'cardigan welsh')
                    """.formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
                                st.executeUpdate(insertDml);
                                var messages = List.of(createMessage(), createMessage());
                                this.messageQueue.push(messages, conn);
                                this.messageQueue.push(messages, conn); // trigger pk constraint violation
                            } catch (SQLException e) {
                                assertUniquePrimaryKeyViolation(dataSource, e);
                                throw new RuntimeException(e);
                            }
                        }));
        sleepJitterForTransactionPropagation(dataSource);
        try (var conn = this.getConnection()) {
            assertMessages(
                    Collections.emptyList(), this.messageQueue.read(10, conn), "failed txn changes not committed");
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testReadAuditMetadataUpdated(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = createMessages(5);
        push(dataSource, messages);
        try (var conn = this.getConnection()) {
            var result = this.messageQueue.read(10, conn);
            assertMessages(messages, result);
        }
        int expectedReadCount = 1;
        assertAuditMetadata(messages, expectedReadCount);
        try (var conn = this.getConnection()) {
            this.messageQueue.read(10, conn);
            assertAuditMetadata(messages, expectedReadCount + 1);
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testCreateIndexIfNotExists(DataSource dataSource) throws SQLException {
        configure(dataSource);
        try (var conn = this.getConnection()) {
            this.messageQueue.createTableWithSchemaIfNotExists(conn);
        }
        tearDown(dataSource);
    }

    private void assertAuditMetadata(List<Message> expectedMessages, int expectedRowCount) throws SQLException {
        try (var conn = this.getConnection();
                var st = conn.createStatement()) {
            var userRs = st.executeQuery("SELECT CURRENT_USER");
            userRs.next();
            var expectedUser = userRs.getString(1);
            var sql = """
                    SELECT * FROM "%s"."%s"
                    WHERE "read_count" > 0
                    AND "read_by" IS NOT NULL
                    """.formatted(this.messageQueue.tableSchemaName(), this.messageQueue.queueTableName());
            var rs = st.executeQuery(sql);
            assertTrue(rs.isBeforeFirst());
            var actual = new ArrayList<Message>();
            while (rs.next()) {
                var readCount = rs.getInt("read_count");
                var readUser = rs.getString("read_by");
                assertEquals(expectedRowCount, readCount);
                assertEquals(expectedUser.toUpperCase(), readUser.toUpperCase());
                actual.add(Message.builder()
                        .id(rs.getString("id"))
                        .data(rs.getString("data"))
                        .build());
            }
            assertMessages(expectedMessages, actual);
        }
    }
}
