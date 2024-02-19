package corg.io.postgres.mq.consumer;

import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageHandlerConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.table.MessageQueueTable;
import corg.io.postgres.mq.table.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

public class MessageHandlerLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerLifecycle.class);

    private final MessageQueueTable messageQueueTable;
    private final MessageHandlerConfig messageHandlerConfig;
    private final MessageHandler messageHandler;
    private final TransactionManager transactionManager;

    public MessageHandlerLifecycle(DbConfig dbConfig, MessageQueueConfig messageQueueConfig,
                                   MessageHandlerConfig messageHandlerConfig, MessageHandler messageHandler) {
        this.messageQueueTable = MessageQueueTable.of(dbConfig, messageQueueConfig);
        this.messageHandlerConfig = Objects.requireNonNull(messageHandlerConfig);
        this.messageHandler = Objects.requireNonNull(messageHandler);
        this.transactionManager = new TransactionManager();
    }

    public void listen() throws SQLException {
        this.transactionManager.executeInTransaction(() -> {
            try {
                return this.messageQueueTable.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, conn -> {
            try {
                var messages = this.messageQueueTable.getPendingMessages(this.messageHandlerConfig.maxNumMessages(), conn);
                if(!messages.isEmpty()) {
                    var handled = this.messageHandler.handle(messages);
                    this.messageQueueTable.dequeue(handled, conn);
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
