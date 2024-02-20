package corg.io.postgres.mq.handler;

import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageHandlerConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import corg.io.postgres.mq.model.message.MessageHandlerBatch;
import corg.io.postgres.mq.table.MessageQueueTable;
import corg.io.postgres.mq.table.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final MessageQueueTable messageQueueTable;
    private final MessageHandlerConfig messageHandlerConfig;
    private final TransactionManager transactionManager;

    public static MessageHandler of(DbConfig dbConfig, MessageQueueConfig messageQueueConfig,
                                                 MessageHandlerConfig messageHandlerConfig) {
        return new MessageHandler(dbConfig, messageQueueConfig, messageHandlerConfig);
    }

    private MessageHandler(DbConfig dbConfig, MessageQueueConfig messageQueueConfig,
                           MessageHandlerConfig messageHandlerConfig) {
        this.messageQueueTable = MessageQueueTable.of(dbConfig, messageQueueConfig);
        this.messageHandlerConfig = Objects.requireNonNull(messageHandlerConfig);
        this.transactionManager = new TransactionManager();
    }

    public void listen(Function<MessageHandlerBatch, List<Message>> handler) throws SQLException {
        this.transactionManager.executeInTransaction(() -> {
            try {
                return this.messageQueueTable.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, transactionConnection -> {
            try {
                var messages = this.messageQueueTable.getPendingMessages(this.messageHandlerConfig.maxNumMessages(), transactionConnection);
                if(!messages.isEmpty()) {
                    var handled = handler.apply(MessageHandlerBatch.of(messages, transactionConnection));
                    this.messageQueueTable.dequeue(handled, transactionConnection);
                }
                else {
                    logger.debug("No messages returned to handle");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
