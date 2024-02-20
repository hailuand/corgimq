package corg.io.postgres.mq.table.datasource;

import corg.io.postgres.mq.RelationalTestUtil;
import corg.io.postgres.mq.table.MessageQueueTest;
import org.junit.jupiter.api.AfterEach;

import java.sql.SQLException;

public class H2MessageQueueTest extends MessageQueueTest {
    @Override
    protected String getUserName() {
        return RelationalTestUtil.h2UserName();
    }

    @Override
    protected String getPassword() {
        return RelationalTestUtil.getH2Password();
    }


    @Override
    protected String getJdbcUrl() {
        return RelationalTestUtil.h2JdbcUrl();
    }

    @Override
    protected void assertUniquePrimaryKeyViolation(SQLException exception) {
        RelationalTestUtil.assertH2PrimaryKeyViolation(exception);
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = getConnection()) {
            RelationalTestUtil.cleanupH2(conn);
        }
    }
}
