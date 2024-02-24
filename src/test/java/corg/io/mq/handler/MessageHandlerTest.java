package corg.io.mq.handler;

import corg.io.mq.AbstractMessageQueueTest;
import corg.io.mq.model.config.DbConfig;
import corg.io.mq.model.config.MessageQueueConfig;
import corg.io.mq.model.config.MessageHandlerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class MessageHandlerTest extends AbstractMessageQueueTest {
    protected MessageHandler messageHandler;

    @BeforeEach
    public void setupMessageHandlerTest() {
        var dbConfig = DbConfig.of(getJdbcUrl(), getUserName(), getPassword());
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        var mhConfig = MessageHandlerConfig.of(30);
        this.messageHandler = MessageHandler.of(dbConfig, mqConfig, mhConfig);
    }

    @Test
    public void testHandle() {
        fail("n/i");
    }

    @Test
    public void testHandleNoMessages() {
        fail("n/i");
    }

    @Test
    public void testHandleInTransaction() {
        fail("n/i");
    }

    @Test
    public void testHandleInTransactionFailed() {
        fail("n/i");
    }
}
