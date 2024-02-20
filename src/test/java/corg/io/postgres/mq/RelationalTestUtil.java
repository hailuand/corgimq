package corg.io.postgres.mq;

import org.h2.api.ErrorCode;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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

    public static String postgresUserName(JdbcDatabaseContainer<?> jdbcContainer) {
        return jdbcContainer.getUsername();
    }

    public static String postgresPassword(JdbcDatabaseContainer<?> jdbcContainer) {
        return jdbcContainer.getPassword();
    }

    public static void assertPostgresPrimaryKeyViolation(SQLException sqlException) {
        assertInstanceOf(PSQLException.class, sqlException);
        assertEquals(PSQLState.UNIQUE_VIOLATION.getState(), sqlException.getSQLState());
    }

    // -- H2
    public static String h2JdbcUrl() {
        return H2_JDBC_URL;
    }

    public static String h2UserName() {
        return H2_USER_NAME;
    }

    public static String getH2Password() {
        return H2_PASSWORD;
    }

    public static void cleanupH2(Connection connection) throws SQLException {
        try(var st = connection.createStatement()) {
            st.execute("DROP ALL OBJECTS");
        }
    }

    public static void assertH2PrimaryKeyViolation(SQLException exception) {
        assertInstanceOf(JdbcSQLIntegrityConstraintViolationException.class, exception);
        assertEquals(ErrorCode.DUPLICATE_KEY_1, Integer.parseInt(exception.getSQLState()), "unique violation");
    }
}
