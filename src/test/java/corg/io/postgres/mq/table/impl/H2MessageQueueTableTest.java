package corg.io.postgres.mq.table.impl;

import corg.io.postgres.mq.table.AbstractMessageQueueTableTest;
import org.junit.jupiter.api.AfterEach;

import java.sql.SQLException;
import java.util.Properties;

public class H2MessageQueueTableTest extends AbstractMessageQueueTableTest {
    private static final String H2_JDBC_URL = "jdbc:h2:mem:TEST_DB" +
            ";DATABASE_TO_UPPER=false;mode=mysql;LOCK_TIMEOUT=10000;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER_NAME = "sa";
    private static final String H2_PASSWORD = "";

    @Override
    protected Properties getProps() {
        var props = new Properties();
        props.setProperty("user", H2_USER_NAME);
        props.setProperty("password", H2_PASSWORD);
        return props;
    }

    @Override
    protected String getJdbcUrl() {
        return H2_JDBC_URL;
    }

    @AfterEach
    public void testTearDown() throws SQLException {
        try(var conn = getConnection(); var st = conn.createStatement()) {
            st.execute("DROP ALL OBJECTS");
        }
    }
}
