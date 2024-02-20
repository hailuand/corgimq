package corg.io.postgres.mq;

import corg.io.postgres.mq.model.config.DbConfig;
import corg.io.postgres.mq.model.config.MessageQueueConfig;
import corg.io.postgres.mq.model.message.Message;
import corg.io.postgres.mq.table.MessageQueueTable;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractMessageQueueTableTest {
    protected static final String QUEUE_NAME = "corgi";
    private static final Faker faker = new Faker();
    protected abstract String getUserName();
    protected abstract String getPassword();
    protected abstract String getJdbcUrl();

    protected MessageQueueTable messageQueue;

    @BeforeEach
    public void testSetup() {
        var dbConfig = DbConfig.of(getJdbcUrl(), getUserName(), getPassword());
        var mqConfig = MessageQueueConfig.of(QUEUE_NAME);
        messageQueue = MessageQueueTable.of(dbConfig, mqConfig);
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
        return Message.of(UUID.randomUUID().toString(), data, Instant.now(Clock.systemUTC()), Optional.empty());
    }
}
