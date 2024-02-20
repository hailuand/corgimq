package corg.io.postgres.mq.table.datasource;

import corg.io.postgres.mq.RelationalTestUtil;
import corg.io.postgres.mq.table.MessageQueueTableTestHarness;
import org.junit.jupiter.api.AfterEach;

import java.sql.SQLException;
import java.util.Properties;

public class H2MessageQueueTableTest extends MessageQueueTableTestHarness {

    @Override
    protected Properties getProps() {
        return RelationalTestUtil.h2Props();
    }

    @Override
    protected String getJdbcUrl() {
        return RelationalTestUtil.h2JdbcUrl();
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = getConnection()) {
            RelationalTestUtil.cleanupPostgres(conn, messageQueue.tableSchemaName());
        }
    }
}
