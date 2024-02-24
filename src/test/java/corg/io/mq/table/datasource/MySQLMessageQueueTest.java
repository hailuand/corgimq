package corg.io.mq.table.datasource;

import corg.io.mq.RelationalTestUtil;
import corg.io.mq.table.MessageQueueTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

@Testcontainers(disabledWithoutDocker = true)
public class MySQLMessageQueueTest extends MessageQueueTest {
    private static JdbcDatabaseContainer<?> mySql;

    @BeforeAll
    public static void setupMySql() {
        mySql = RelationalTestUtil.mySQLContainer("corgio");
        mySql.start();
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = getConnection()) {
            RelationalTestUtil.cleanupMySql(conn, messageQueue.tableSchemaName());
        }
    }

    @Override
    protected String getUserName() {
        return mySql.getUsername();
    }

    @Override
    protected String getPassword() {
        return mySql.getPassword();
    }

    @Override
    protected String getJdbcUrl() {
        return "%s?sessionVariables=sql_mode=ANSI_QUOTES".formatted(mySql.getJdbcUrl());
    }

    @Override
    protected void assertUniquePrimaryKeyViolation(SQLException exception) {
        RelationalTestUtil.assertMySQLPrimaryKeyViolation(exception);
    }
}
