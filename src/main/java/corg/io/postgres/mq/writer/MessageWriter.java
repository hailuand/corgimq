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

    public static MessageWriter of(DbConfig dbConfig, MessageQueueConfig messageQueueConfig) throws SQLException {
        return new MessageWriter(dbConfig, messageQueueConfig);
    }

    private MessageWriter(DbConfig dbConfig, MessageQueueConfig messageQueueConfig) throws SQLException {
        this.messageQueueTable = MessageQueueTable.of(dbConfig, messageQueueConfig);
        this.messageQueueTable.initSources();
    }

    public void write(List<Message> messages, Connection conn) throws SQLException {
        if(messages.isEmpty()) {
            logger.warn("No messages provided");
            return;
        }
        logger.debug("Writing {} messages", messages.size());
        this.messageQueueTable.push(messages, conn);
    }
}
