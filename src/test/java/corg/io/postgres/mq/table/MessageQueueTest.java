package corg.io.postgres.mq.table;

import corg.io.postgres.mq.AbstractMessageQueueTest;
import corg.io.postgres.mq.model.message.Message;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SqlSourceToSinkFlow")
public abstract class MessageQueueTest extends AbstractMessageQueueTest {

    @Test
    public void testInitSources() throws SQLException {
        assertRowCount(0);
    }

    @Test
    public void testPop() throws SQLException {
        assertRowCount(0);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = messageQueue.getConnection()) {
            messageQueue.push(messages, conn);
            assertRowCount(messages.size());
            var pending = messageQueue.read(10, conn);
            assertMessages(messages, pending);
        }
    }

    @Test
    public void testPopAndRead() throws SQLException {
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = messageQueue.getConnection()) {
            messageQueue.push(messages, conn);
            assertRowCount(messages.size());
            var pending = messageQueue.read(10, conn);
            assertMessages(messages, pending);
        }
        // Pop in another txn
        try(var conn = messageQueue.getConnection()) {
            messageQueue.pop(messages, conn);
            var pendingPostPop = messageQueue.read(10, conn);
            assertTrue(pendingPostPop.isEmpty());
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
                processed.add(Message.builder().id(id).data(data).build());
            }
            var messagesMap = messages.stream().collect(Collectors.toMap(Message::id, Function.identity()));
            for(var processedMessage : processed) {
                assertTrue(messagesMap.containsKey(processedMessage.id()));
                var unprocessedMessage = messagesMap.get(processedMessage.id());
                assertEquals(unprocessedMessage, processedMessage);
            }
        }
    }

    @Test
    public void testWriteDupes() throws SQLException {
        var messages = List.of(createMessage(), createMessage());
        this.messageQueue.push(messages, getConnection());
        assertThrows(SQLException.class, () -> {
            try {
                this.messageQueue.push(messages, getConnection());
            }
            catch(SQLException e) {
                assertUniquePrimaryKeyViolation(e);
                throw e;
            }
        });
    }

    @Test
    public void testPushNothing() throws SQLException {
        assertTrue(this.messageQueue.read(10, getConnection()).isEmpty());
        this.messageQueue.push(Collections.emptyList(), getConnection());
        assertTrue(this.messageQueue.read(10, getConnection()).isEmpty());
    }

    @Test
    public void testPushTransaction() throws SQLException {
        String secondaryTableName = "varieties";
        createSecondaryTable(secondaryTableName);
        var messages = List.of(createMessage(), createMessage());
        new TransactionManager().executeInTransaction(() -> {
            try {
                return this.messageQueue.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, conn -> {
            try(var st = conn.createStatement()) {
                // work on a different table as part of txn
                var insertDml = """
                    INSERT INTO "%s"."%s" VALUES
                    (1, 'pembroke welsh'),
                    (2, 'cardigan welsh')
                    """.formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
                st.executeUpdate(insertDml);
                this.messageQueue.push(messages, conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        assertMessages(this.messageQueue.read(10, messageQueue.getConnection()), messages);
    }

    @Test
    public void testPushTransactionFails() throws SQLException {
        String secondaryTableName = "varieties";
        createSecondaryTable(secondaryTableName);
        assertThrows(RuntimeException.class, () -> new TransactionManager().executeInTransaction(() -> {
            try {
                return this.messageQueue.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, conn -> {
            try(var st = conn.createStatement()) {
                // work on a different table as part of txn
                var insertDml = """
                    INSERT INTO "%s"."%s" VALUES
                    (1, 'pembroke welsh'),
                    (2, 'cardigan welsh')
                    """.formatted(this.messageQueue.tableSchemaName(), secondaryTableName);
                st.executeUpdate(insertDml);
                var messages = List.of(createMessage(), createMessage());
                this.messageQueue.push(messages, conn);
                this.messageQueue.push(messages, conn); // trigger pk constraint violation
            } catch (SQLException e) {
                assertUniquePrimaryKeyViolation(e);
                throw new RuntimeException(e);
            }
        }));
        assertMessages(this.messageQueue.read(10, messageQueue.getConnection()),
                Collections.emptyList(), "failed txn changes not committed");
    }

    private void createSecondaryTable(String secondaryTableName) throws SQLException {
        try(var st = this.messageQueue.getConnection().createStatement()) {
            var ddl = """
                    CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "id" INTEGER PRIMARY KEY,
                    "variety" TEXT NOT NULL
                    );
                    """.formatted(
                    messageQueue.tableSchemaName(),
                    secondaryTableName);
            st.execute(ddl);
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
