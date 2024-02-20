package corg.io.postgres.mq.table.impl;

import corg.io.postgres.mq.table.AbstractMessageQueueTableTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.SQLException;
import java.util.Properties;

@Testcontainers(disabledWithoutDocker = true)
public class PostgresMessageQueueTableTest extends AbstractMessageQueueTableTest {
    private static PostgreSQLContainer<?> postgres;

    @BeforeAll
    static void setupPostgres() {
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres")
                .withTag("16.2"));
        postgres.start();
    }

    @Override
    protected Properties getProps() {
        var props = new Properties();
        props.setProperty("user", postgres.getUsername());
        props.setProperty("password", postgres.getPassword());
        return props;
    }

    @Override
    protected String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = getConnection(); var st = conn.createStatement()) {
            st.execute("DROP SCHEMA %s CASCADE".formatted(underTest.tableSchemaName()));
        }
    }

    @AfterAll
    static void shutdownPostgres() {
        postgres.close();
    }
}
