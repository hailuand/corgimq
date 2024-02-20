package corg.io.postgres.mq;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class RelationalTestUtil {
    private static final String H2_JDBC_URL = "jdbc:h2:mem:TEST_DB" +
            ";DATABASE_TO_UPPER=false;mode=mysql;LOCK_TIMEOUT=10000;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER_NAME = "sa";
    private static final String H2_PASSWORD = "";

    // -- PostgreSQL
    public static JdbcDatabaseContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres")
                .withTag("16.2"));
    }

    public static void cleanupPostgres(Connection connection, String schema) throws SQLException {
        try(var st = connection.createStatement()) {
            st.execute("DROP SCHEMA %s CASCADE".formatted(schema));
        }
    }

    public static Properties jdbcContainerProps(JdbcDatabaseContainer<?> jdbcContainer) {
        var props = new Properties();
        props.setProperty("user", jdbcContainer.getUsername());
        props.setProperty("password", jdbcContainer.getPassword());
        return props;
    }

    // -- H2
    public static String h2JdbcUrl() {
        return H2_JDBC_URL;
    }

    public static Properties h2Props() {
        var props = new Properties();
        props.setProperty("user", H2_USER_NAME);
        props.setProperty("password", H2_PASSWORD);
        return props;
    }

    public static void cleanupH2(Connection connection) throws SQLException {
        try(var st = connection.createStatement()) {
            st.execute("DROP ALL OBJECTS");
        }
    }
}
