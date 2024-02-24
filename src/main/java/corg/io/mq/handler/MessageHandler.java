package corg.io.mq.handler;

import corg.io.mq.model.config.MessageHandlerConfig;
import corg.io.mq.model.message.Message;
import corg.io.mq.model.message.MessageHandlerBatch;
import corg.io.mq.table.MessageQueue;
import corg.io.mq.table.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final MessageQueue messageQueue;
    private final MessageHandlerConfig messageHandlerConfig;
    private final TransactionManager transactionManager;

    public static MessageHandler of(MessageQueue messageQueue, MessageHandlerConfig messageHandlerConfig) {
        return new MessageHandler(messageQueue, messageHandlerConfig);
    }

    private MessageHandler(MessageQueue messageQueue, MessageHandlerConfig messageHandlerConfig) {
        this.messageQueue = Objects.requireNonNull(messageQueue);
        this.messageHandlerConfig = Objects.requireNonNull(messageHandlerConfig);
        this.transactionManager = new TransactionManager();
    }

    public void listen(Function<MessageHandlerBatch, List<Message>> handler) throws SQLException {
        this.transactionManager.executeInTransaction(() -> {
            try {
                return this.messageQueue.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, transactionConnection -> {
            try {
                var messages = this.messageQueue.read(this.messageHandlerConfig.maxNumMessages(), transactionConnection);
                if(!messages.isEmpty()) {
                    var handled = handler.apply(MessageHandlerBatch.of(messages, transactionConnection));
                    this.messageQueue.pop(handled, transactionConnection);
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
