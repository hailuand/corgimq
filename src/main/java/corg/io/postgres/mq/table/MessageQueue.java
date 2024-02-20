package corg.io.postgres.mq.table;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MessageQueue {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private final MessageQueueConfig messageQueueConfig;
    private final HikariDataSource hikariDataSource;

    public static MessageQueue of(DbConfig dbConfig, MessageQueueConfig messageQueueConfig) {
        return new MessageQueue(dbConfig, messageQueueConfig);
    }

    private MessageQueue(DbConfig dbConfig, MessageQueueConfig messageQueueConfig) {
        Objects.requireNonNull(dbConfig);
        this.messageQueueConfig = Objects.requireNonNull(messageQueueConfig);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbConfig.jdbcUrl());
        hikariConfig.setUsername(dbConfig.username());
        hikariConfig.setPassword(dbConfig.password());
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void initSources() throws SQLException {
        this.createTableWithSchema();
    }

    public void push(List<Message> messages, Connection conn) throws SQLException {
        var dml = """
                INSERT INTO "%s"."%s" ("id", "data", "message_time")
                VALUES (?, ?, ?)
                """.formatted(
                this.tableSchemaName(),
                this.queueTableName()
        );
        logger.debug(dml);
        try(var statement = conn.prepareStatement(dml)) {
            for(var message : messages) {
                statement.setString(1, message.id());
                statement.setString(2, message.data());
                statement.setTimestamp(3, Timestamp.from(message.messageTime()));
                statement.addBatch();
            }
            statement.executeBatch();
        }
        catch(SQLException e) {
            logger.debug("Exception occurred pushing messages", e);
            throw e.getNextException(); // Root exception serves as a wrapper
        }
    }

    public void pop(List<Message> messages, Connection conn) throws SQLException {
        var dml = """
                    UPDATE "%s"."%s" SET "processing_time" = ?
                    WHERE "id" = ?
                    AND "processing_time" IS NULL
                    """.formatted(this.tableSchemaName(), this.queueTableName());
        logger.debug(dml);
        try(var statement = conn.prepareStatement(dml)) {
            var processingTime = Instant.now(Clock.systemUTC());
            for(var message : messages) {
                statement.setTimestamp(1, Timestamp.from(processingTime));
                statement.setString(2, message.id());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public List<Message> read(int numMessages, Connection conn) throws SQLException {
        var sql = """
                SELECT * FROM "%s"."%s"
                WHERE "processing_time" IS NULL
                ORDER BY "message_time" ASC
                LIMIT %d
                FOR UPDATE SKIP LOCKED
                """.formatted(
                        this.tableSchemaName(),
                this.queueTableName(),
                numMessages
        );
        List<Message> pending = new ArrayList<>();
        try(var statement = conn.createStatement()) {
            logger.debug(sql);
            var rs = statement.executeQuery(sql);
            while(rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                var messageTime = rs.getTimestamp("message_time").toInstant();
                pending.add(Message.of(id, data, messageTime, Optional.empty()));
            }
        }
        return pending;
    }

    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    public String tableSchemaName() {
        return "corgio";
    }
    public String queueTableName() {
        return "%s_mq".formatted(this.messageQueueConfig.queueName());
    }

    private void createTableWithSchema() throws SQLException {
        var ddl = """
                CREATE SCHEMA IF NOT EXISTS "%s";
                
                CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "id" TEXT PRIMARY KEY,
                    "data" TEXT NOT NULL,
                    "message_time" TIMESTAMP NOT NULL,
                    "processing_time" TIMESTAMP
                );
                """.formatted(
                this.tableSchemaName(),
                this.tableSchemaName(),
                this.queueTableName()
        );
        try(var conn = getConnection(); var statement = conn.createStatement()) {
            logger.debug(ddl);
            statement.execute(ddl);
        }
    }
}
