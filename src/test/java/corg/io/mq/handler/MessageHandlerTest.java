package corg.io.mq.handler;

import corg.io.mq.AbstractMessageQueueTest;
import corg.io.mq.model.config.MessageHandlerConfig;
import corg.io.mq.model.message.Message;
import corg.io.mq.model.message.MessageHandlerBatch;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageHandlerTest extends AbstractMessageQueueTest {
    private static final int MAX_MESSAGES = 15;
    private MessageHandler messageHandler;

    @Override
    protected void configure(DataSource dataSource) throws SQLException {
        super.configure(dataSource);
        this.messageHandler = MessageHandler.of(this.messageQueue, MessageHandlerConfig.of(MAX_MESSAGES));
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandle(DataSource dataSource) throws SQLException {
        configure(dataSource);
        var messages = List.of(createMessage(), createMessage(), createMessage());
        try(var conn = this.messageQueue.getConnection()) {
            this.messageQueue.push(messages, conn);
        }
        var handled = new ArrayList<Message>();
        assertMessagesInTable(messages, false);
        this.messageHandler.listen(batch -> {
            handled.addAll(batch.messages());
            return handled;
        });
        assertMessagesInTable(messages, true);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleNoMessages(DataSource dataSource) throws SQLException {
        configure(dataSource);
        assertTableRowCount(0);
        try(var conn = this.messageQueue.getConnection()) {
            this.messageQueue.push(Collections.emptyList(), conn);
        }
        assertTableRowCount(0);
        tearDown(dataSource);
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleInTransaction(DataSource dataSource) {
        fail("n/i");
    }

    @ParameterizedTest
    @EnumSource(DataSource.class)
    public void testHandleInTransactionFailed(DataSource dataSource) {
        fail("n/i");
    }

    private void assertMessagesInTable(List<Message> messages, boolean processed) throws SQLException {
        try(var conn = this.messageQueue.getConnection(); var st = conn.createStatement()) {
            var processedClause = "";
            if(processed) {
                processedClause = "AND \"processing_time\" IS NOT NULL";
            }
            var sql = """
                    SELECT * from "%s"."%s"
                    WHERE "id" IN %s
                    %s
                    ORDER BY "message_time" ASC
                    """
                    .formatted(
                            this.messageQueue.tableSchemaName(),
                            this.messageQueue.queueTableName(),
                            messages.stream()
                                    .map(Message::id)
                                    .map("'%s'"::formatted)
                                    .toList()
                                    .toString()
                                    .replace('[', '(')
                                    .replace(']', ')'),
                            processedClause
                    );
            var rs = st.executeQuery(sql);
            var inTable = new HashMap<>();
            while(rs.next()) {
                var id = rs.getString("id");
                var data = rs.getString("data");
                inTable.put(id, Message.builder().id(id).data(data).build());
            }
            assertEquals(messages.size(), inTable.size());
            for(var msg : messages) {
                assertEquals(msg, inTable.get(msg.id()));
            }
        }
    }
}
