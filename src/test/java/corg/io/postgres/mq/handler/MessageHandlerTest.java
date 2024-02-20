package corg.io.postgres.mq.handler;

import corg.io.postgres.mq.AbstractMessageQueueTest;
import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageHandlerConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
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
