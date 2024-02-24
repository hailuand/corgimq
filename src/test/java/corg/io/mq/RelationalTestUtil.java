package corg.io.mq;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.h2.api.ErrorCode;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

public final class RelationalTestUtil {
    private static final String H2_JDBC_URL = "jdbc:h2:mem:TEST_DB;DATABASE_TO_UPPER=false;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
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
        assertNotNull(sqlException.getNextException());
        var psqlException = sqlException.getNextException();
        assertInstanceOf(PSQLException.class, psqlException);
        assertEquals(PSQLState.UNIQUE_VIOLATION.getState(), psqlException.getSQLState());
    }

    // -- MySQL
    public static JdbcDatabaseContainer<?> mySQLContainer(String schemaName) {
        return new MySQLContainer<>(DockerImageName.parse("mysql")
                .withTag("8.3.0"))
                .withUsername("root")
                .withPassword("")
                .withEnv("MYSQL_ROOT_HOST", "%");
    }

    public static void assertMySQLPrimaryKeyViolation(SQLException sqlException) {
        assertInstanceOf(BatchUpdateException.class, sqlException);
        assertEquals(MysqlErrorNumbers.SQL_STATE_INTEGRITY_CONSTRAINT_VIOLATION, sqlException.getSQLState());
        assertNotNull(sqlException.getCause());
        assertInstanceOf(SQLIntegrityConstraintViolationException.class, sqlException.getCause());
    }

    public static void cleanupMySql(Connection connection, String schema) throws SQLException {
        try(var st = connection.createStatement()) {
            st.execute("DROP SCHEMA %s".formatted(schema));
        }
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
        assertNotNull(exception.getNextException());
        var h2Exception = exception.getNextException();
        assertInstanceOf(JdbcSQLIntegrityConstraintViolationException.class, h2Exception);
        assertEquals(ErrorCode.DUPLICATE_KEY_1, Integer.parseInt(h2Exception.getSQLState()), "unique violation");
    }
}
