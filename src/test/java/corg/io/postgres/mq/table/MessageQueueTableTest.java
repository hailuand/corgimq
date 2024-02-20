package corg.io.postgres.mq.table;

import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SqlSourceToSinkFlow")
public class MessageQueueTableTest {
    static final String QUEUE_NAME = "corgi";
    static final String TEST_DATABASE = "TEST_DB";
    static final String H2_JDBC_URL = "jdbc:h2:mem:" + TEST_DATABASE +
            ";DATABASE_TO_UPPER=false;mode=mysql;LOCK_TIMEOUT=10000;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
    static final String H2_USER_NAME = "sa";
    static final String H2_PASSWORD = "";
    static final Faker faker = new Faker();

    MessageQueueTable underTest;

    @BeforeEach
    public void testSetup() throws SQLException {
        var props = new Properties();
        props.setProperty("user", H2_USER_NAME);
        props.setProperty("password", H2_PASSWORD);
        var dbConfig = DbConfig.of(H2_JDBC_URL, props);
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        underTest = MessageQueueTable.of(dbConfig, mqConfig);
        underTest.initSources();
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = underTest.getConnection(); var st = conn.createStatement()) {
            st.execute("DROP ALL OBJECTS");
        }
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
                var count = rs.getLong("count(*)");
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
