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

package corg.io.mq.table;

import static org.junit.jupiter.api.Assertions.*;

import corg.io.mq.AbstractMessageQueueTest;
import corg.io.mq.model.message.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        messageQueue.push(messages);
        assertTableRowCount(messages.size());
        var pending = messageQueue.read(10);
        assertMessages(messages, pending);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPopAndRead(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        messageQueue.push(messages);
        assertTableRowCount(messages.size());
        var pending = messageQueue.read(10);
        assertMessages(messages, pending);
        // Pop in another txn
        messageQueue.pop(messages);
        var pendingPostPop = messageQueue.read(10);
        Assertions.assertTrue(pendingPostPop.isEmpty());

        // assert in separate txn
        try (var conn = messageQueue.getConnection();
                var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT * from \"%s\".\"%s\" ORDER BY \"message_time\" ASC"
                    .formatted(messageQueue.tableSchemaName(), messageQueue.queueTableName()));
            Assertions.assertTrue(rs.isBeforeFirst());
            List<Message> processed = new ArrayList<>();
            while (rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                processed.add(Message.builder().id(id).data(data).build());
            }
            var messagesMap = messages.stream().collect(Collectors.toMap(Message::id, Function.identity()));
            for (var processedMessage : processed) {
                assertTrue(messagesMap.containsKey(processedMessage.id()));
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
        this.messageQueue.push(messages);
        assertThrows(SQLException.class, () -> {
            try {
                this.messageQueue.push(messages);
            } catch (SQLException e) {
                assertUniquePrimaryKeyViolation(dataSource, e);
                throw e;
            }
        });
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushNothing(DataSource dataSource) throws SQLException {
        configure(dataSource);
        Assertions.assertTrue(this.messageQueue.read(10).isEmpty());
        this.messageQueue.push(Collections.emptyList());
        Assertions.assertTrue(this.messageQueue.read(10).isEmpty());
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushTransaction(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "varieties";
        createSecondaryTable(secondaryTableName);
        var messages = List.of(createMessage(), createMessage());
        new TransactionManager()
                .executeInTransaction(
                        () -> {
                            try {
                                return this.messageQueue.getConnection();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        conn -> {
                            try (var st = conn.createStatement()) {
                                // work on a different table as part of txn
                                var insertDml =
                                        """
                    INSERT INTO "%s"."%s" VALUES
                    (1, 'pembroke welsh'),
                    (2, 'cardigan welsh')
                    """
                                                .formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
                                st.executeUpdate(insertDml);
                                this.messageQueue.push(messages, conn);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
        assertMessages(this.messageQueue.read(10), messages);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushTransactionFails(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "varieties";
        createSecondaryTable(secondaryTableName);
        assertThrows(RuntimeException.class, () -> new TransactionManager()
                .executeInTransaction(
                        () -> {
                            try {
                                return this.messageQueue.getConnection();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        conn -> {
                            try (var st = conn.createStatement()) {
                                // work on a different table as part of txn
                                var insertDml =
                                        """
                    INSERT INTO "%s"."%s" VALUES
                    (1, 'pembroke welsh'),
                    (2, 'cardigan welsh')
                    """
                                                .formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
                                st.executeUpdate(insertDml);
                                var messages = List.of(createMessage(), createMessage());
                                this.messageQueue.push(messages, conn);
                                this.messageQueue.push(messages, conn); // trigger pk constraint violation
                            } catch (SQLException e) {
                                assertUniquePrimaryKeyViolation(dataSource, e);
                                throw new RuntimeException(e);
                            }
                        }));
        assertMessages(this.messageQueue.read(10), Collections.emptyList(), "failed txn changes not committed");
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testReadAuditMetadataUpdated(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = createMessages(5);
        this.messageQueue.push(messages);
        var result = this.messageQueue.read(10);
        assertMessages(messages, result);
        int expectedReadCount = 1;
        assertAuditMetadata(messages, expectedReadCount);
        this.messageQueue.read(10);
        assertAuditMetadata(messages, expectedReadCount + 1);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testAdditionalDataSourceProperties(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var mqWithProperties = MessageQueue.of(
                dbConfig.withAdditionalDataSourceProperties(Map.of("cachePrepStmts", "true")), mqConfig);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        mqWithProperties.push(messages);
        assertTableRowCount(messages.size());
        var pending = mqWithProperties.read(10);
        assertMessages(messages, pending);
        tearDown(dataSource);
    }

    private void assertAuditMetadata(List<Message> expectedMessages, int expectedRowCount) throws SQLException {
        try (var conn = this.messageQueue.getConnection();
                var st = conn.createStatement()) {
            var userRs = st.executeQuery("SELECT CURRENT_USER");
            userRs.next();
            var expectedUser = userRs.getString("CURRENT_USER");
            var sql =
                    """
                    SELECT * FROM "%s"."%s"
                    WHERE "read_count" > 0
                    AND "read_by" IS NOT NULL
                    """
                            .formatted(this.messageQueue.tableSchemaName(), this.messageQueue.queueTableName());
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
