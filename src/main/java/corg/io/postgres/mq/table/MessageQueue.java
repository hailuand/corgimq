package corg.io.postgres.mq.table;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageQueue implements Closeable, AutoCloseable {
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
                INSERT INTO "%s"."%s" ("id", "data")
                VALUES (?, ?)
                """.formatted(
                this.tableSchemaName(),
                this.queueTableName()
        );
        logger.debug(dml);
        try(var statement = conn.prepareStatement(dml)) {
            for(var message : messages) {
                statement.setString(1, message.id());
                statement.setString(2, message.data());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public void pop(List<Message> messages, Connection conn) throws SQLException {
        var dml = """
                    UPDATE "%s"."%s" SET "processing_time" = CURRENT_TIMESTAMP
                    WHERE "id" = ?
                    AND "processing_time" IS NULL
                    """.formatted(this.tableSchemaName(), this.queueTableName());
        logger.debug(dml);
        try(var statement = conn.prepareStatement(dml)) {
            for(var message : messages) {
                statement.setString(1, message.id());
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
                pending.add(Message.builder().id(id).data(data).build());
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
        var createSchemaDdl = """
                CREATE SCHEMA IF NOT EXISTS "%s";
                """.formatted(this.tableSchemaName());
        var createTableddl = """                
                CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "id" VARCHAR(36) PRIMARY KEY,
                    "data" TEXT NOT NULL,
                    "message_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                    "processing_time" TIMESTAMP
                );
                """.formatted(
                this.tableSchemaName(),
                this.queueTableName()
        );
        try(var conn = getConnection(); var statement = conn.createStatement()) {
            logger.debug(createSchemaDdl);
            statement.execute(createSchemaDdl);
            logger.debug(createTableddl);
            statement.execute(createTableddl);
        }
    }

    @Override
    public void close() {
        this.hikariDataSource.close();
    }
}
