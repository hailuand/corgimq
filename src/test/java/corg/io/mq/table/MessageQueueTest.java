package corg.io.mq.table;

import corg.io.mq.AbstractMessageQueueTest;
import corg.io.mq.model.message.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SqlSourceToSinkFlow")
public class MessageQueueTest extends AbstractMessageQueueTest {
    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testInitSources(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(0);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPop(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(0);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = this.messageQueue.getConnection()) {
            messageQueue.push(messages, conn);
            assertTableRowCount(messages.size());
            var pending = messageQueue.read(10, conn);
            assertMessages(messages, pending);
        }
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPopAndRead(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = messageQueue.getConnection()) {
            messageQueue.push(messages, conn);
            assertTableRowCount(messages.size());
            var pending = messageQueue.read(10, conn);
            assertMessages(messages, pending);
        }
        // Pop in another txn
        try(var conn = messageQueue.getConnection()) {
            messageQueue.pop(messages, conn);
            var pendingPostPop = messageQueue.read(10, conn);
            Assertions.assertTrue(pendingPostPop.isEmpty());
        }
        // assert in separate txn
        try(var conn = messageQueue.getConnection(); var st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT * from \"%s\".\"%s\" ORDER BY \"message_time\" ASC"
                    .formatted(messageQueue.tableSchemaName(), messageQueue.queueTableName()));
            Assertions.assertTrue(rs.isBeforeFirst());
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
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testWriteDupes(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = List.of(createMessage(), createMessage());
        this.messageQueue.push(messages, this.messageQueue.getConnection());
        assertThrows(SQLException.class, () -> {
            try {
                this.messageQueue.push(messages, this.messageQueue.getConnection());
            }
            catch(SQLException e) {
                assertUniquePrimaryKeyViolation(dataSource, e);
                throw e;
            }
        });
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushNothing(DataSource dataSource) throws SQLException {
        configure(dataSource);
        Assertions.assertTrue(this.messageQueue.read(10, this.messageQueue.getConnection()).isEmpty());
        this.messageQueue.push(Collections.emptyList(), this.messageQueue.getConnection());
        Assertions.assertTrue(this.messageQueue.read(10, this.messageQueue.getConnection()).isEmpty());
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushTransaction(DataSource dataSource) throws SQLException {
        configure(dataSource);
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
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testPushTransactionFails(DataSource dataSource) throws SQLException {
        configure(dataSource);
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
                assertUniquePrimaryKeyViolation(dataSource, e);
                throw new RuntimeException(e);
            }
        }));
        assertMessages(this.messageQueue.read(10, messageQueue.getConnection()),
                Collections.emptyList(), "failed txn changes not committed");
        tearDown(dataSource);
    }
}
