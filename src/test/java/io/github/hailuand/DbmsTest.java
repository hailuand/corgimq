/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.github.hailuand;

import static org.junit.jupiter.api.Assertions.fail;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.hailuand.corgi.mq.MessageQueue;
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.testcontainers.cockroachdb.CockroachContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mssqlserver.MSSQLServerContainer;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class DbmsTest {
    private static final String H2_JDBC_URL =
            "jdbc:h2:mem:TEST_DB;DATABASE_TO_UPPER=false;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER_NAME = "sa";
    private static final String H2_PASSWORD = "";

    private JdbcDatabaseContainer<?> jdbcContainer;
    protected MessageQueueConfig mqConfig;
    private HikariDataSource hikariDataSource;

    @Container
    private static final JdbcDatabaseContainer<?> postgres =
            new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("16.2"));

    @Container
    private static final JdbcDatabaseContainer<?> mySql = new MySQLContainer(
                    DockerImageName.parse("mysql").withTag("8.3.0"))
            .withUsername("root")
            .withPassword("")
            .withEnv("MYSQL_ROOT_HOST", "%");

    @Container
    private static final JdbcDatabaseContainer<?> cockroachDb = new CockroachContainer(
            DockerImageName.parse("cockroachdb/cockroach").withTag("v23.1.16"));

    @Container
    private static final JdbcDatabaseContainer<?> mssql = new MSSQLServerContainer(
                    DockerImageName.parse("mcr.microsoft.com/mssql/server").withTag("2022-CU10-ubuntu-22.04"))
            .acceptLicense();

    @Container
    private static final JdbcDatabaseContainer<?> oracleFree =
            new OracleContainer(DockerImageName.parse("gvenzl/oracle-free").withTag("23.4-slim-faststart"));

    @Container
    private static final JdbcDatabaseContainer<?> oracleXe = new org.testcontainers.containers.OracleContainer(
                    DockerImageName.parse("gvenzl/oracle-xe").withTag("21-slim-faststart"))
            .withStartupTimeout(Duration.ofMinutes(10));

    protected void configure(AbstractMessageQueueTest.DataSource dataSource) throws SQLException {
        switch (dataSource) {
            case COCKROACHDB -> this.jdbcContainer = cockroachDb;
            case POSTGRES -> this.jdbcContainer = postgres;
            case MYSQL -> this.jdbcContainer = mySql;
            case MSSQL -> this.jdbcContainer = mssql;
            case ORACLE_FREE -> this.jdbcContainer = oracleFree;
            case ORACLE_XE -> this.jdbcContainer = oracleXe;
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.getJdbcUrl(dataSource));
        hikariConfig.setUsername(this.getUserName(dataSource));
        hikariConfig.setPassword(this.getPassword(dataSource));
        hikariConfig.setPoolName("CorgiMQ Test Pool");
        hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(2));
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    protected Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    protected void tearDown(AbstractMessageQueueTest.DataSource dataSource, MessageQueue messageQueue)
            throws SQLException {
        try (var conn = this.getConnection();
                var st = conn.createStatement()) {
            switch (dataSource) {
                case H2 -> st.execute("DROP ALL OBJECTS");
                case COCKROACHDB, POSTGRES ->
                    st.execute("DROP SCHEMA %s CASCADE".formatted(messageQueue.tableSchemaName()));
                case MYSQL -> st.execute("DROP SCHEMA %s".formatted(messageQueue.tableSchemaName()));
                case MSSQL -> {
                    String dropScript = """
        DECLARE @sql NVARCHAR(MAX) = '';
        SELECT @sql += 'ALTER TABLE [%s].[' + OBJECT_NAME(parent_object_id) + '] DROP CONSTRAINT [' + name + ']; '
        FROM sys.foreign_keys WHERE SCHEMA_NAME(schema_id) = '%s';
        SELECT @sql += 'DROP TABLE [%s].[' + name + ']; '
        FROM sys.tables WHERE SCHEMA_NAME(schema_id) = '%s';
        IF @sql <> '' EXEC sp_executesql @sql;
        DROP SCHEMA IF EXISTS [%s];
        """.formatted(
                                    messageQueue.tableSchemaName(),
                                    messageQueue.tableSchemaName(),
                                    messageQueue.tableSchemaName(),
                                    messageQueue.tableSchemaName(),
                                    messageQueue.tableSchemaName());
                    st.execute(dropScript);
                }
                case ORACLE_FREE, ORACLE_XE -> {
                    String dropScript = """
                DECLARE
                BEGIN
                    FOR tbl IN (SELECT table_name FROM user_tables) LOOP
                        EXECUTE IMMEDIATE 'DROP TABLE "' || tbl.table_name || '" CASCADE CONSTRAINTS';
                    END LOOP;

                    FOR seq IN (SELECT sequence_name FROM user_sequences) LOOP
                        EXECUTE IMMEDIATE 'DROP SEQUENCE "' || seq.sequence_name || '"';
                    END LOOP;
                END;
                """;
                    st.execute(dropScript);
                }

                default -> fail("Not implemented: %s".formatted(dataSource.name()));
            }
        }
        this.hikariDataSource.close();
    }

    protected String getUserName(AbstractMessageQueueTest.DataSource dataSource) {
        if (dataSource == DataSource.H2) {
            return H2_USER_NAME;
        }
        return this.jdbcContainer.getUsername();
    }

    protected String getPassword(AbstractMessageQueueTest.DataSource dataSource) {
        if (dataSource == DataSource.H2) {
            return H2_PASSWORD;
        }
        return this.jdbcContainer.getPassword();
    }

    protected String getJdbcUrl(AbstractMessageQueueTest.DataSource dataSource) {
        return switch (dataSource) {
            case H2 -> H2_JDBC_URL;
            case MYSQL -> "%s?sessionVariables=sql_mode=ANSI_QUOTES".formatted(this.jdbcContainer.getJdbcUrl());
            case MSSQL -> "%s;quotedIdentifier=on".formatted(this.jdbcContainer.getJdbcUrl());
            case COCKROACHDB, POSTGRES, ORACLE_FREE, ORACLE_XE -> this.jdbcContainer.getJdbcUrl();
        };
    }

    protected void assertUniquePrimaryKeyViolation(
            AbstractMessageQueueTest.DataSource dataSource, SQLException exception) {
        switch (dataSource) {
            case H2 -> RelationalTestUtil.assertH2PrimaryKeyViolation(exception);
            case MYSQL -> RelationalTestUtil.assertMySQLPrimaryKeyViolation(exception);
            case MSSQL -> RelationalTestUtil.assertMsSQLPrimaryKeyViolation(exception);
            case COCKROACHDB, POSTGRES -> RelationalTestUtil.assertPostgresPrimaryKeyViolation(exception);
            case ORACLE_FREE, ORACLE_XE -> RelationalTestUtil.assertOracleDbPrimaryKeyViolation(exception);
            default -> fail("Not implemented: %s".formatted(dataSource.name()));
        }
    }

    public enum DataSource {
        H2,
        COCKROACHDB,
        MYSQL,
        MSSQL,
        ORACLE_FREE,
        ORACLE_XE,
        POSTGRES,
    }
}
