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

package io.github.hailuand.corgi.mq.handler;

import static org.junit.jupiter.api.Assertions.*;

import io.github.hailuand.AbstractMessageQueueTest;
import io.github.hailuand.corgi.mq.model.config.MessageHandlerConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import io.github.hailuand.corgi.mq.model.message.MessageHandlerBatch;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MessageHandlerTest extends AbstractMessageQueueTest {
    private static final int MAX_MESSAGES = 15;
    private MessageHandler messageHandler;

    @Override
    protected void configure(DataSource dataSource) throws SQLException {
        super.configure(dataSource);
        this.messageHandler = MessageHandler.of(this.messageQueue, MessageHandlerConfig.of(MAX_MESSAGES));
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandle(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var defaultHandler = MessageHandler.of(this.messageQueue);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        push(dataSource, messages);
        var handled = new ArrayList<Message>();
        assertMessagesInTable(dataSource, messages, false);
        listen(dataSource, defaultHandler, batch -> {
            handled.addAll(batch.messages());
            return handled;
        });
        assertMessagesInTable(dataSource, messages, true);
        handled.clear();
        listen(dataSource, defaultHandler, batch -> {
            handled.addAll(batch.messages());
            return handled;
        });
        assertTrue(handled.isEmpty());
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleNoMessages(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(dataSource, 0);
        push(dataSource, Collections.emptyList());
        assertTableRowCount(dataSource, 0);
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleInTransaction(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "handler_results";
        createSecondaryTable(dataSource, secondaryTableName);
        final var messages = List.of(createMessage(), createMessage(), createMessage());
        push(dataSource, messages);
        var handled = new ArrayList<Message>();
        var tableName = """
                "%s"."%s"
                """.formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
        if (dataSource == DataSource.ORACLE_FREE || dataSource == DataSource.ORACLE_XE) {
            tableName = secondaryTableName;
        }
        var dml = """
               INSERT INTO %s VALUES
                (?, ?)
               """.formatted(tableName);
        var secondaryDataMapping = new HashMap<Integer, String>();
        listen(dataSource, this.messageHandler, batch -> {
            try (var st = batch.transactionConnection().prepareStatement(dml)) {
                int id = 0;
                for (var msg : batch.messages()) {
                    st.setInt(1, id);
                    st.setString(2, msg.data());
                    st.addBatch();
                    handled.add(msg);
                    secondaryDataMapping.put(id++, msg.data());
                }
                st.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return handled;
        });
        assertMessagesInTable(dataSource, messages, true);
        // assert results of other txn
        try (var conn = this.getConnection();
                var st = conn.createStatement()) {
            var query = "SELECT * FROM %s ORDER BY \"id\" ASC".formatted(tableName);
            var rs = st.executeQuery(query);
            assertTrue(rs.isBeforeFirst());

            while (rs.next()) {
                var id = rs.getInt("id");
                var otherData = rs.getString("some_data");
                assertEquals(secondaryDataMapping.get(id), otherData);
            }
        }
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleInTransactionFailed(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "handler_results";
        createSecondaryTable(dataSource, secondaryTableName);
        final var messages = List.of(createMessage(), createMessage(), createMessage());
        push(dataSource, messages);
        var tableName = """
                "%s"."%s"
                """.formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
        if (dataSource == DataSource.ORACLE_FREE || dataSource == DataSource.ORACLE_XE) {
            tableName = secondaryTableName;
        }
        var dml = """
               INSERT INTO %s VALUES
                (?, ?)
               """.formatted(tableName);
        assertThrows(
                RuntimeException.class,
                () -> listen(dataSource, this.messageHandler, batch -> {
                    var handled = new ArrayList<Message>();
                    try (var st = batch.transactionConnection().prepareStatement(dml)) {
                        int id = 0;
                        for (var msg : batch.messages()) {
                            st.setInt(1, id);
                            st.setString(2, msg.data());
                            st.addBatch();
                            handled.add(msg);
                        }
                        st.executeBatch();
                    } catch (SQLException e) {
                        assertUniquePrimaryKeyViolation(dataSource, e);
                        throw new RuntimeException(e);
                    }
                    return handled;
                }));
        sleepJitterForTransactionPropagation(dataSource);
        assertMessagesInTable(dataSource, messages, false); // no messages popped
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleSqlExceptionSql(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "handler_results";
        createSecondaryTable(dataSource, secondaryTableName);
        final var messages = List.of(createMessage(), createMessage(), createMessage());
        push(dataSource, messages);
        assertThrows(
                RuntimeException.class,
                () -> listen(dataSource, this.messageHandler, batch -> {
                    try (var conn = batch.transactionConnection()) {
                        var st = conn.createStatement();
                        var sql = "select * from foobar";
                        st.execute(sql);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }));
        assertMessagesInTable(dataSource, messages, false); // no messages popped
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testMessageBatchSize(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = createMessages(10);
        int batchSize = 4;
        push(dataSource, messages);
        var handler = MessageHandler.of(this.messageQueue, MessageHandlerConfig.of(batchSize));
        var msgsMap = messages.stream().collect(Collectors.toMap(Message::id, Function.identity()));
        var processed = new ArrayList<Message>();
        for (int i = 0; i < Math.ceilDiv(messages.size(), batchSize); i++) {
            int expectedSize = Math.min(messages.size() - (batchSize * i), batchSize);
            listen(dataSource, handler, batch -> {
                var batched = batch.messages();
                Assertions.assertEquals(expectedSize, batched.size());
                var expected = new ArrayList<Message>();
                for (var msg : batched) {
                    Assertions.assertTrue(msgsMap.containsKey(msg.id()));
                    expected.add(msgsMap.get(msg.id()));
                    msgsMap.remove(msg.id());
                }
                assertMessages(expected, batched);
                processed.addAll(batched);
                return batched;
            });
            assertMessagesInTable(dataSource, processed, true);
        }
        Assertions.assertTrue(msgsMap.isEmpty());
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testSubsetOfMessagesHandled(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = createMessages(10);
        var processing = new ArrayList<Message>();
        processing.add(messages.get(0));
        processing.add(messages.get(1));
        push(dataSource, messages);
        listen(dataSource, messageHandler, batch -> {
            assertTrue(processing.size() < batch.messages().size());
            Assertions.assertTrue(batch.messages().containsAll(processing));
            return processing;
        });
        assertMessagesInTable(dataSource, processing, true);
        messages.removeAll(processing);
        assertMessagesInTable(dataSource, messages, false);
        tearDown(dataSource, this.messageQueue);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testMultiThreading(DataSource dataSource) throws SQLException, InterruptedException {
        configure(dataSource);
        var messages = createMessages(10);
        var handler = MessageHandler.of(this.messageQueue, MessageHandlerConfig.of(3));
        push(dataSource, messages);
        var threadMessages1 = new HashSet<Message>();
        var threadMessages2 = new HashSet<Message>();
        var t1 = new Thread(() -> listen(dataSource, handler, batch -> {
            threadMessages1.addAll(batch.messages());
            return batch.messages();
        }));
        var t2 = new Thread(() -> listen(dataSource, handler, batch -> {
            threadMessages2.addAll(batch.messages());
            return batch.messages();
        }));
        Thread.sleep(200);
        t1.start();
        Thread.sleep(200);
        t2.start();
        t1.join();
        t2.join();
        assertFalse(threadMessages1.isEmpty());
        assertFalse(threadMessages2.isEmpty());
        for (var message : threadMessages1) {
            assertFalse(threadMessages2.contains(message));
        }
        assertMessagesInTable(dataSource, new ArrayList<>(threadMessages1), true);
        assertMessagesInTable(dataSource, new ArrayList<>(threadMessages2), true);
        var unprocessedMessages = messages.stream()
                .filter(m -> !threadMessages1.contains(m) && !threadMessages2.contains(m))
                .toList();
        assertMessagesInTable(dataSource, unprocessedMessages, false);
        tearDown(dataSource, this.messageQueue);
    }

    private void assertMessagesInTable(DataSource dataSource, List<Message> messages, boolean processed)
            throws SQLException {
        try (var conn = this.getConnection();
                var st = conn.createStatement()) {
            var processedClause = "";
            if (processed) {
                processedClause = """
                        AND "processing_time" IS NOT NULL
                        AND "read_count" > 0
                        AND "read_by" IS NOT NULL
                        """;
            } else {
                processedClause = "AND \"processing_time\" IS NULL";
            }
            var tableName = """
                    "%s"."%s"
                    """.formatted(this.messageQueue.tableSchemaName(), this.messageQueue.queueTableName());
            if (dataSource == DataSource.ORACLE_FREE || dataSource == DataSource.ORACLE_XE) {
                tableName = this.messageQueue.queueTableName();
            }
            var sql = """
                    SELECT * from %s
                    WHERE "id" IN %s
                    %s
                    ORDER BY "message_time" ASC
                    """.formatted(
                            tableName,
                            messages.stream()
                                    .map(Message::id)
                                    .map("'%s'"::formatted)
                                    .toList()
                                    .toString()
                                    .replace('[', '(')
                                    .replace(']', ')'),
                            processedClause);
            var rs = st.executeQuery(sql);
            var inTable = new HashMap<>();
            while (rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                inTable.put(id, Message.builder().id(id).data(data).build());
            }
            assertEquals(messages.size(), inTable.size());
            for (var msg : messages) {
                assertEquals(msg, inTable.get(msg.id()));
            }
        }
    }

    private void listen(
            DataSource dataSource, MessageHandler handler, Function<MessageHandlerBatch, List<Message>> func) {
        try {
            handler.listen(
                    () -> {
                        try {
                            return this.getConnection();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    func);
            sleepJitterForTransactionPropagation(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
