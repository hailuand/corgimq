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

package corg.io.mq.handler;

import static org.junit.jupiter.api.Assertions.*;

import corg.io.mq.AbstractMessageQueueTest;
import corg.io.mq.model.config.MessageHandlerConfig;
import corg.io.mq.model.message.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("SqlDialectInspection")
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
        this.messageQueue.push(messages);
        var handled = new ArrayList<Message>();
        assertMessagesInTable(messages, false);
        defaultHandler.listen(batch -> {
            handled.addAll(batch.messages());
            return handled;
        });
        assertMessagesInTable(messages, true);
        handled.clear();
        defaultHandler.listen(batch -> {
            handled.addAll(batch.messages());
            return handled;
        });
        assertTrue(handled.isEmpty());
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleNoMessages(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(0);
        this.messageQueue.push(Collections.emptyList());
        assertTableRowCount(0);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleInTransaction(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "handler_results";
        createSecondaryTable(secondaryTableName);
        final var messages = List.of(createMessage(), createMessage(), createMessage());
        this.messageQueue.push(messages);
        var handled = new ArrayList<Message>();
        var dml = """
               INSERT INTO "%s"."%s" VALUES
                (?, ?)
                """
                .formatted(SCHEMA_NAME, secondaryTableName);
        var secondaryDataMapping = new HashMap<Integer, String>();
        this.messageHandler.listen(batch -> {
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
        assertMessagesInTable(messages, true);
        // assert results of other txn
        try (var conn = this.messageQueue.getConnection();
                var st = conn.createStatement()) {
            var rs = st.executeQuery(
                    "SELECT * FROM \"%s\".\"%s\" ORDER BY \"id\" ASC".formatted(SCHEMA_NAME, secondaryTableName));
            assertTrue(rs.isBeforeFirst());

            while (rs.next()) {
                var id = rs.getInt("id");
                var otherData = rs.getString("some_data");
                assertEquals(secondaryDataMapping.get(id), otherData);
            }
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleInTransactionFailed(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "handler_results";
        createSecondaryTable(secondaryTableName);
        final var messages = List.of(createMessage(), createMessage(), createMessage());
        this.messageQueue.push(messages);
        var dml = """
               INSERT INTO "%s"."%s" VALUES
                (?, ?)
                """
                .formatted(SCHEMA_NAME, secondaryTableName);
        assertThrows(
                RuntimeException.class,
                () -> this.messageHandler.listen(batch -> {
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
        assertMessagesInTable(messages, false); // no messages popped
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleSqlExceptionSql(DataSource dataSource) throws SQLException {
        configure(dataSource);
        String secondaryTableName = "handler_results";
        createSecondaryTable(secondaryTableName);
        final var messages = List.of(createMessage(), createMessage(), createMessage());
        this.messageQueue.push(messages);
        assertThrows(
                RuntimeException.class,
                () -> this.messageHandler.listen(batch -> {
                    try (var st = batch.transactionConnection().createStatement()) {
                        var sql = "select * from foobar";
                        st.execute(sql);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }));
        assertMessagesInTable(messages, false); // no messages popped
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testMessageBatchSize(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var msgs = createMessages(10);
        int batchSize = 4;
        this.messageQueue.push(msgs);
        var handler = MessageHandler.of(this.messageQueue, MessageHandlerConfig.of(batchSize));
        var msgsMap = msgs.stream().collect(Collectors.toMap(Message::id, Function.identity()));
        var processed = new ArrayList<Message>();
        for (int i = 0; i < Math.ceilDiv(msgs.size(), batchSize); i++) {
            int expectedSize = Math.min(msgs.size() - (batchSize * i), batchSize);
            handler.listen(batch -> {
                var batched = batch.messages();
                assertEquals(expectedSize, batched.size());
                var expected = new ArrayList<Message>();
                for (var msg : batched) {
                    assertTrue(msgsMap.containsKey(msg.id()));
                    expected.add(msgsMap.get(msg.id()));
                    msgsMap.remove(msg.id());
                }
                assertMessages(expected, batched);
                processed.addAll(batched);
                return batched;
            });
            assertMessagesInTable(processed, true);
        }
        assertTrue(msgsMap.isEmpty());
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testSubsetOfMessagesHandled(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var msgs = createMessages(10);
        var processing = new ArrayList<Message>();
        processing.add(msgs.get(0));
        processing.add(msgs.get(1));
        this.messageQueue.push(msgs);
        this.messageHandler.listen(batch -> {
            assertTrue(processing.size() < batch.messages().size());
            assertTrue(batch.messages().containsAll(processing));
            return processing;
        });
        assertMessagesInTable(processing, true);
        msgs.removeAll(processing);
        assertMessagesInTable(msgs, false);
        tearDown(dataSource);
    }

    private void assertMessagesInTable(List<Message> messages, boolean processed) throws SQLException {
        try (var conn = this.messageQueue.getConnection();
                var st = conn.createStatement()) {
            var processedClause = "";
            if (processed) {
                processedClause =
                        """
                        AND "processing_time" IS NOT NULL
                        AND "read_count" > 0
                        AND "read_by" IS NOT NULL
                        """;
            } else {
                processedClause = "AND \"processing_time\" IS NULL";
            }
            var sql =
                    """
                    SELECT * from "%s"."%s"
                    WHERE "id" IN %s
                    %s
                    ORDER BY "message_time" ASC
                    """
                            .formatted(
                                    this.messageQueue.tableSchemaName(),
                                    this.messageQueue.queueTableName(),
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
}
