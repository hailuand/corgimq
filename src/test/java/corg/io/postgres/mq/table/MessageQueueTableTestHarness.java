package corg.io.postgres.mq.table;

import corg.io.postgres.mq.AbstractMessageQueueTableTest;
import corg.io.postgres.mq.model.message.Message;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SqlSourceToSinkFlow")
public abstract class MessageQueueTableTestHarness extends AbstractMessageQueueTableTest {

    @Test
    public void testInitSources() throws SQLException {
        assertRowCount(0);
    }

    @Test
    public void testEnqueue() throws SQLException {
        assertRowCount(0);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = messageQueue.getConnection()) {
            messageQueue.enqueue(messages, conn);
            assertRowCount(messages.size());
            var pending = messageQueue.getPendingMessages(10, conn);
            assertEquals(messages, pending);
        }
    }

    @Test
    public void testDequeueAndGetPending() throws SQLException {
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = messageQueue.getConnection()) {
            messageQueue.enqueue(messages, conn);
            assertRowCount(messages.size());
            var pending = messageQueue.getPendingMessages(10, conn);
            assertEquals(messages, pending);
        }
        // dequeue in another txn
        try(var conn = messageQueue.getConnection()) {
            messageQueue.dequeue(messages, conn);
            var pendingPostDequeue = messageQueue.getPendingMessages(10, conn);
            assertTrue(pendingPostDequeue.isEmpty());
        }
        // assert in separate txn
        try(var conn = messageQueue.getConnection(); var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT * from \"%s\".\"%s\" ORDER BY \"message_time\" ASC"
                    .formatted(messageQueue.tableSchemaName(), messageQueue.queueTableName()));
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
        try(var conn = messageQueue.getConnection(); var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT COUNT(*) from \"%s\".\"%s\"".formatted(messageQueue.tableSchemaName(),
                    messageQueue.queueTableName()));
            assertTrue(rs.isBeforeFirst());
            if(rs.next()) {
                var count = rs.getLong(1);
                assertEquals(expectedRowCount, count, "Row count matches");
            }
        }
    }
}
