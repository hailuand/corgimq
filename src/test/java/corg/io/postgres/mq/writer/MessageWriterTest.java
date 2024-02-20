package corg.io.postgres.mq.writer;

import corg.io.postgres.mq.AbstractMessageQueueTableTest;
import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class MessageWriterTest extends AbstractMessageQueueTableTest {
    protected MessageWriter messageWriter;

    @BeforeEach
    public void setupMessageWriterTest() throws SQLException {
        var dbConfig = DbConfig.of(getJdbcUrl(), getUserName(), getPassword());
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        this.messageWriter = MessageWriter.of(dbConfig, mqConfig);
    }

    @Test
    public void testWrite() throws SQLException {
        assertTrue(this.messageQueue.getPendingMessages(10, getConnection()).isEmpty());
        var messages = List.of(createMessage(), createMessage());
        this.messageWriter.write(messages, getConnection());
        assertEquals(messages, this.messageQueue.getPendingMessages(10, getConnection()));
    }

    @Test
    public void testWriteDupes() throws SQLException {
        var messages = List.of(createMessage(), createMessage());
        this.messageWriter.write(messages, getConnection());
        assertThrows(SQLException.class, () -> {
            this.messageWriter.write(messages, getConnection());
            // todo - some kind of exception visitor that makes sure that this is a pk constraint violation
        });
    }

    @Test
    public void testWriteNothing() throws SQLException {
        assertTrue(this.messageQueue.getPendingMessages(10, getConnection()).isEmpty());
        this.messageWriter.write(Collections.emptyList(), getConnection());
        assertTrue(this.messageQueue.getPendingMessages(10, getConnection()).isEmpty());
    }
}
