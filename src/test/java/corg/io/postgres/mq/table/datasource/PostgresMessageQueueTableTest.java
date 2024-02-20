package corg.io.postgres.mq.table.datasource;

import corg.io.postgres.mq.RelationalTestUtil;
import corg.io.postgres.mq.table.MessageQueueTableTestHarness;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Properties;

@Testcontainers(disabledWithoutDocker = true)
public class PostgresMessageQueueTableTest extends MessageQueueTableTestHarness {
    private static JdbcDatabaseContainer<?> postgres;

    @BeforeAll
    static void setupPostgres() {
        postgres = RelationalTestUtil.postgresContainer();
        postgres.start();
    }

    @Override
    protected Properties getProps() {
        return RelationalTestUtil.jdbcContainerProps(postgres);
    }

    @Override
    protected String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = getConnection()) {
            RelationalTestUtil.cleanupPostgres(conn, messageQueue.tableSchemaName());
        }
    }

    @AfterAll
    static void shutdownPostgres() {
        postgres.close();
    }
}
