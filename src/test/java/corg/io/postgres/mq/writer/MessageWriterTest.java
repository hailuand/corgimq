package corg.io.postgres.mq.writer;

import corg.io.postgres.mq.AbstractMessageQueueTableTest;
import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.table.TransactionManager;
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
            try {
                this.messageWriter.write(messages, getConnection());
            }
            catch(SQLException e) {
                assertUniquePrimaryKeyViolation(e);
                throw e;
            }
        });
    }

    @Test
    public void testWriteNothing() throws SQLException {
        assertTrue(this.messageQueue.getPendingMessages(10, getConnection()).isEmpty());
        this.messageWriter.write(Collections.emptyList(), getConnection());
        assertTrue(this.messageQueue.getPendingMessages(10, getConnection()).isEmpty());
    }

    @Test
    public void testWriteTransaction() throws SQLException {
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
                this.messageWriter.write(messages, conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(this.messageQueue.getPendingMessages(10, messageQueue.getConnection()), messages);
    }

    @Test
    public void testWriteTransactionFails() throws SQLException {
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
                this.messageWriter.write(messages, conn);
                this.messageWriter.write(messages, conn); // trigger pk constraint violation
            } catch (SQLException e) {
                assertUniquePrimaryKeyViolation(e);
                throw new RuntimeException(e);
            }
        }));
        assertEquals(this.messageQueue.getPendingMessages(10, messageQueue.getConnection()),
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
}
