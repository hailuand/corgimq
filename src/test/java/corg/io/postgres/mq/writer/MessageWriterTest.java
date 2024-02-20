package corg.io.postgres.mq.writer;

import corg.io.postgres.mq.AbstractMessageQueueTableTest;
import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class MessageWriterTest extends AbstractMessageQueueTableTest {
    protected MessageWriter messageWriter;

    @BeforeEach
    public void setupMessageWriterTest() {
        var props = getProps();
        var dbConfig = DbConfig.of(getJdbcUrl(), props);
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        this.messageWriter = MessageWriter.of(dbConfig, mqConfig);
    }

    @Test
    public void testWrite() throws SQLException {
        assertTrue(this.messageQueue.getPendingMessages(10, getConnection()).isEmpty());
        var messages = List.of(createMessage(), createMessage());
        this.messageWriter.write(messages, getConnection());
    }

    @Test
    public void testWriteDupes() {
        fail("n/i");
    }

    @Test
    public void testWriteNothing() {
        fail("n/i");
    }
}
