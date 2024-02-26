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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import corg.io.mq.model.config.DatabaseConfig;
import corg.io.mq.model.config.MessageQueueConfig;
import corg.io.mq.model.message.Message;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class MessageQueue implements Closeable, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);
    private static final String POOL_NAME = "CorgiMQPool";

    private final MessageQueueConfig messageQueueConfig;
    private final HikariDataSource hikariDataSource;

    public static MessageQueue of(DatabaseConfig dbConfig, MessageQueueConfig messageQueueConfig) {
        return new MessageQueue(dbConfig, messageQueueConfig);
    }

    private MessageQueue(DatabaseConfig dbConfig, MessageQueueConfig messageQueueConfig) {
        Objects.requireNonNull(dbConfig);
        this.messageQueueConfig = Objects.requireNonNull(messageQueueConfig);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbConfig.jdbcUrl());
        hikariConfig.setUsername(dbConfig.username());
        hikariConfig.setPassword(dbConfig.password());
        hikariConfig.setMaximumPoolSize(dbConfig.maxConnectionPoolSize());
        hikariConfig.setMaxLifetime(dbConfig.connectionMaxLifetime());
        hikariConfig.setPoolName(POOL_NAME);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void initSources() throws SQLException {
        this.createTableWithSchema();
    }

    public void push(List<Message> messages) throws SQLException {
        this.push(messages, this.getConnection());
    }

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

    public List<Message> read(int numMessages) throws SQLException {
        return this.read(numMessages, getConnection());
    }

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

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    public String tableSchemaName() {
        return this.messageQueueConfig.schemaName();
    }

    public String queueTableName() {
        return "%s_q".formatted(this.messageQueueConfig.queueName());
    }

    private void createTableWithSchema() throws SQLException {
        configureMDC();
        var createSchemaDdl = """
                CREATE SCHEMA IF NOT EXISTS "%s";
                """
                .formatted(this.messageQueueConfig.schemaName());
        var createTableddl =
                """
                CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "id" VARCHAR(36) PRIMARY KEY,
                    "data" TEXT NOT NULL,
                    "message_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                    "read_count" INTEGER DEFAULT 0 NOT NULL,
                    "read_by" VARCHAR(16),
                    "processing_time" TIMESTAMP
                );
                """
                        .formatted(this.tableSchemaName(), this.queueTableName());
        try (var conn = getConnection();
                var statement = conn.createStatement()) {
            logger.debug(createSchemaDdl);
            statement.execute(createSchemaDdl);
            logger.debug(createTableddl);
            statement.execute(createTableddl);
        }
        MDC.clear();
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

    @Override
    public void close() {
        this.hikariDataSource.close();
    }
}
