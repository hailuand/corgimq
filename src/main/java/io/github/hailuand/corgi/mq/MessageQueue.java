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

import io.github.hailuand.corgi.mq.handler.MessageHandler;
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import io.github.hailuand.corgi.mq.sql.dialect.SqlDialect;
import io.github.hailuand.corgi.mq.sql.dialect.SqlDialectFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Interface for managing a message queue in a DBMS. Allows for the creation of a queue,
 * and pushing + popping of a {@link Message} directly.
 * See {@link MessageHandler} for how to encapsulate reading and processing messages.
 */
public class MessageQueue {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);
    private static final String SCHEMA_NAME = "mq";

    private final MessageQueueConfig messageQueueConfig;
    private final SqlDialect sqlDialect;

    /**
     * Creates a new instance of a message queue, inquiring the database to build the appropriate {@link SqlDialect}.
     * @param messageQueueConfig Configuration to use for creating queue
     * @param conn {@link Connection} to database
     * @return New {@link MessageQueue} instance
     * @throws SQLException If database connection unavailable
     */
    public static MessageQueue of(MessageQueueConfig messageQueueConfig, Connection conn) throws SQLException {
        return new MessageQueue(messageQueueConfig, SqlDialectFactory.fromConnection(conn));
    }

    /**
     * Creates the schema and message queue table if they don't yet exist.
     * @param conn {@link Connection} to database
     * @throws SQLException If unable to execute creation DDL
     */
    public void createTableWithSchemaIfNotExists(Connection conn) throws SQLException {
        configureMDC();
        var createSchemaDdl = this.sqlDialect.schemaDdl(this.tableSchemaName());
        var createTableDdl = this.sqlDialect
                .tableDdl(this.tableSchemaName(), this.queueTableName())
                .formatted(this.tableSchemaName(), this.queueTableName());
        try (var statement = conn.createStatement()) {
            logger.debug(createSchemaDdl);
            statement.execute(createSchemaDdl);
            logger.debug(createTableDdl);
            statement.execute(createTableDdl);

            var rs = statement.executeQuery(
                    this.sqlDialect.checkIndexExistenceDql(this.tableSchemaName(), this.queueTableName()));
            rs.next();
            boolean indexNotCreated = rs.getInt("count") == 0;
            if (indexNotCreated) {
                var createIndexDdl = this.sqlDialect.indexDdl(this.tableSchemaName(), this.queueTableName());
                logger.debug(createIndexDdl);
                statement.execute(createIndexDdl);
            }
        }
        MDC.clear();
    }

    /**
     * Pushes {@link Message} to the queue.
     * @param messages Collection of {@link Message} to push
     * @param conn {@link Connection} to database
     * @throws SQLException if database-level exception occurs while pushing messages
     */
    public void push(List<Message> messages, Connection conn) throws SQLException {
        configureMDC();
        if (messages.isEmpty()) {
            logger.info("No messages provided to push.");
            return;
        }

        var dml = this.sqlDialect.pushMessagesDml(this.tableSchemaName(), this.queueTableName());
        logger.info("Pushing {} messages", messages.size());
        logger.debug(dml);
        try (var statement = conn.prepareStatement(dml)) {
            for (var message : messages) {
                statement.setString(1, message.id());
                statement.setString(2, message.data());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        MDC.clear();
    }

    /**
     * Pops {@link Message} from the queue, causing them to no longer be received by any readers.
     * 'Pop' means to mark the messages as processed, not their physical removal.
     * @param messages Collection of {@link Message} to mark read.
     * @param conn {@link Connection} to database
     * @throws SQLException If a database-level exception occurs while popping
     */
    public void pop(List<Message> messages, Connection conn) throws SQLException {
        configureMDC();
        var dml = this.sqlDialect.popMessagesDml(this.tableSchemaName(), this.queueTableName());
        logger.debug(dml);
        logger.info("Popping {} messages", messages.size());
        try (var statement = conn.prepareStatement(dml)) {
            for (var message : messages) {
                statement.setString(1, message.id());
                statement.addBatch();
            }
            statement.executeBatch();
        }
        MDC.clear();
    }

    /**
     * Reads {@code numMessages} of {@link Message} from the queue, ordered by message time. Results in
     * the {@link Message} audit metadata being updated.
     * @param numMessages Maximum number of messages to read from the queue.
     * @param conn {@link Connection} to database
     * @return Available {@link Message} in queue
     * @throws SQLException If a database-level exception occurs during read.
     */
    public List<Message> read(int numMessages, Connection conn) throws SQLException {
        configureMDC();
        var sql = this.sqlDialect.readMessagesDql(this.tableSchemaName(), this.queueTableName(), numMessages);
        logger.debug(sql);
        logger.info("Reading messages...");
        List<Message> pending = new ArrayList<>();
        try (var statement = conn.createStatement()) {
            var rs = statement.executeQuery(sql);
            while (rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                pending.add(Message.builder().id(id).data(data).build());
            }
        }
        markMessagesRead(pending, conn);
        logger.info("{} messages received", pending.size());
        MDC.clear();
        return pending;
    }

    public String tableSchemaName() {
        return SCHEMA_NAME;
    }

    public String queueTableName() {
        return "%s_q".formatted(this.messageQueueConfig.queueName());
    }

    private MessageQueue(MessageQueueConfig messageQueueConfig, SqlDialect sqlDialect) {
        this.messageQueueConfig = Objects.requireNonNull(messageQueueConfig);
        this.sqlDialect = Objects.requireNonNull(sqlDialect);
    }

    private void markMessagesRead(List<Message> messages, Connection conn) throws SQLException {
        var dml = this.sqlDialect.updateReadCountDml(this.tableSchemaName(), this.queueTableName());
        logger.debug(dml);
        try (var statement = conn.prepareStatement(dml)) {
            for (var message : messages) {
                statement.setString(1, message.id());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void configureMDC() {
        MDC.put("queue", " - %s".formatted(this.messageQueueConfig.queueName()));
    }
}
