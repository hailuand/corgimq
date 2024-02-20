package corg.io.postgres.mq.writer;

import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import corg.io.postgres.mq.table.MessageQueueTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MessageWriter {
    private static final Logger logger = LoggerFactory.getLogger(MessageWriter.class);

    private final MessageQueueTable messageQueueTable;

    public static MessageWriter of(DbConfig dbConfig, MessageQueueConfig messageQueueConfig) {
        return new MessageWriter(dbConfig, messageQueueConfig);
    }

    private MessageWriter(DbConfig dbConfig, MessageQueueConfig messageQueueConfig) {
        this.messageQueueTable = MessageQueueTable.of(dbConfig, messageQueueConfig);
        try {
            this.messageQueueTable.initSources();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(List<Message> messages, Connection conn) throws SQLException {
        logger.debug("Writing {} messages", messages.size());
        this.messageQueueTable.enqueue(messages, conn);
    }
}
