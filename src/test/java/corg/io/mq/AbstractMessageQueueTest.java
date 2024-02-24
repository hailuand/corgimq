package corg.io.mq;

import corg.io.mq.model.config.DbConfig;
import corg.io.mq.model.config.MessageQueueConfig;
import corg.io.mq.model.message.Message;
import corg.io.mq.table.MessageQueue;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractMessageQueueTest {
    protected static final String QUEUE_NAME = "corgi";
    private static final Faker faker = new Faker();
    protected abstract String getUserName();
    protected abstract String getPassword();
    protected abstract String getJdbcUrl();
    protected abstract void assertUniquePrimaryKeyViolation(SQLException exception);

    protected MessageQueue messageQueue;

    @BeforeEach
    public void testSetup() throws SQLException {
        var dbConfig = DbConfig.of(getJdbcUrl(), getUserName(), getPassword());
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        this.messageQueue = MessageQueue.of(dbConfig, mqConfig);
        this.initSources();
    }

    protected void initSources() throws SQLException {
        this.messageQueue.initSources();
    }

    protected Connection getConnection() throws SQLException {
        return messageQueue.getConnection();
    }

    protected Message createMessage() {
        var data = """
                {
                    "key": "%s",
                    "value": "%s"
                }
                """.formatted(faker.onePiece().character(), faker.onePiece().akumasNoMi());
        return Message.of(data);
    }

    protected void assertMessages(List<Message> expected, List<Message> actual) {
        assertMessages(expected, actual, null);
    }

    protected void assertMessages(List<Message> expected, List<Message> actual, String assertionMessage) {
        String providedMessage = assertionMessage == null ? "expected and actual messages equal" : assertionMessage;
        assertEquals(expected.size(), actual.size());
        var actualMap = actual.stream().collect(Collectors.toMap(Message::id, Function.identity()));
        for(var message : expected) {
            var actualMessage = actualMap.get(message.id());
            assertEquals(message, actualMessage, providedMessage);
        }
    }
}
