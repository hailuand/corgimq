package corg.io.postgres.mq.table;

import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SqlSourceToSinkFlow")
public abstract class AbstractMessageQueueTableTest {
    private static final String QUEUE_NAME = "corgi";
    private static final Faker faker = new Faker();

    protected abstract Properties getProps();
    protected abstract String getJdbcUrl();

    protected MessageQueueTable underTest;

    protected Connection getConnection() throws SQLException {
        return underTest.getConnection();
    }

    @BeforeEach
    public void testSetup() throws SQLException {
        var props = getProps();
        var dbConfig = DbConfig.of(getJdbcUrl(), props);
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        underTest = MessageQueueTable.of(dbConfig, mqConfig);
        underTest.initSources();
    }

    @Test
    public void testInitSources() throws SQLException {
        assertRowCount(0);
    }

    @Test
    public void testEnqueue() throws SQLException {
        assertRowCount(0);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = underTest.getConnection()) {
            underTest.enqueue(messages, conn);
            assertRowCount(messages.size());
            var pending = underTest.getPendingMessages(10, conn);
            assertEquals(messages, pending);
        }
    }

    @Test
    public void testDequeueAndGetPending() throws SQLException {
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = underTest.getConnection()) {
            underTest.enqueue(messages, conn);
            assertRowCount(messages.size());
            var pending = underTest.getPendingMessages(10, conn);
            assertEquals(messages, pending);
        }
        // dequeue in another txn
        try(var conn = underTest.getConnection()) {
            underTest.dequeue(messages, conn);
            var pendingPostDequeue = underTest.getPendingMessages(10, conn);
            assertTrue(pendingPostDequeue.isEmpty());
        }
        // assert in separate txn
        try(var conn = underTest.getConnection(); var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT * from \"%s\".\"%s\" ORDER BY \"message_time\" ASC"
                    .formatted(underTest.tableSchemaName(), underTest.queueTableName()));
            assertTrue(rs.isBeforeFirst());
            List<Message> processed = new ArrayList<>();
            while(rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                var messageTime = rs.getTimestamp("message_time");
                var processTime = rs.getTimestamp("processing_time");
                processed.add(Message.of(id, data, messageTime.toInstant(), Optional.of(processTime.toInstant())));
            }
            var messagesMap = messages.stream().collect(Collectors.toMap(Message::id, Function.identity()));
            for(var processedMessage : processed) {
                assertTrue(messagesMap.containsKey(processedMessage.id()));
                var unprocessedMessage = messagesMap.get(processedMessage.id());
                assertEquals(processedMessage.messageTime(), unprocessedMessage.messageTime());
                assertEquals(unprocessedMessage.data(), processedMessage.data());
                assertTrue(unprocessedMessage.processingTime().isEmpty());
                assertTrue(processedMessage.processingTime().isPresent());
            }
        }
    }

    private void assertRowCount(int expectedRowCount) throws SQLException {
        try(var conn = underTest.getConnection(); var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT COUNT(*) from \"%s\".\"%s\"".formatted(underTest.tableSchemaName(),
                    underTest.queueTableName()));
            assertTrue(rs.isBeforeFirst());
            if(rs.next()) {
                var count = rs.getLong(1);
                assertEquals(expectedRowCount, count, "Row count matches");
            }
        }
    }

    private Message createMessage() {
        var data = """
                {
                    "key": "%s",
                    "value": "%s"
                }
                """.formatted(faker.onePiece().character(), faker.onePiece().akumasNoMi());
        return Message.of(UUID.randomUUID().toString(), data, Instant.now(Clock.systemUTC()), Optional.empty());
    }
}
