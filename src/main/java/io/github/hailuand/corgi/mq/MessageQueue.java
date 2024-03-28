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

    public static final String SCHEMA_NAME = "mq";

    private final MessageQueueConfig messageQueueConfig;

    /**
     * Creates a new instance of a message queue.
     * @param messageQueueConfig Configuration to use for creating queue
     * @return New {@link MessageQueue} instance
     */
    public static MessageQueue of(MessageQueueConfig messageQueueConfig) {
        return new MessageQueue(messageQueueConfig);
    }

    /**
     * Creates the schema and message queue table if they don't yet exist.
     * @param conn {@link Connection} to database
     * @throws SQLException If unable to execute creation DDL
     */
    public void createTableWithSchemaIfNotExists(Connection conn) throws SQLException {
        configureMDC();
        var createSchemaDdl =
                """
                CREATE SCHEMA IF NOT EXISTS "%s";
                """.formatted(SCHEMA_NAME);
        var ddl =
                """
                CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "read_count" INTEGER DEFAULT 0 NOT NULL,
                    "message_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                    "processing_time" TIMESTAMP,
                    "id" VARCHAR(36) PRIMARY KEY,
                    "read_by" VARCHAR(16),
                    "data" TEXT NOT NULL
                );
                """
                        .formatted(this.tableSchemaName(), this.queueTableName());
        try (var statement = conn.createStatement()) {
            logger.debug(createSchemaDdl);
            statement.execute(createSchemaDdl);
            logger.debug(ddl);
            statement.execute(ddl);
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

        var dml =
                """
                INSERT INTO "%s"."%s" ("id", "data")
                VALUES (?, ?)
                """
                        .formatted(this.tableSchemaName(), this.queueTableName());
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
        var dml =
                """
                    UPDATE "%s"."%s" SET "processing_time" = CURRENT_TIMESTAMP
                    WHERE "id" = ?
                    AND "processing_time" IS NULL
                    """
                        .formatted(this.tableSchemaName(), this.queueTableName());
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
        var sql =
                """
                SELECT * FROM "%s"."%s"
                WHERE "processing_time" IS NULL
                ORDER BY "message_time" ASC
                LIMIT %d
                FOR UPDATE SKIP LOCKED
                """
                        .formatted(this.tableSchemaName(), this.queueTableName(), numMessages);
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

    private MessageQueue(MessageQueueConfig messageQueueConfig) {
        this.messageQueueConfig = Objects.requireNonNull(messageQueueConfig);
    }

    private void markMessagesRead(List<Message> messages, Connection conn) throws SQLException {
        var dml =
                """
                UPDATE "%s"."%s"
                SET "read_count" = "read_count" + 1, "read_by" = CURRENT_USER
                WHERE "id" = ?
                """
                        .formatted(this.tableSchemaName(), this.queueTableName());
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
