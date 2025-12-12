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
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    private static final JdbcDatabaseContainer<?> postgres = DatabaseContainers.POSTGRES;

    @Container
    private static final JdbcDatabaseContainer<?> mySql = DatabaseContainers.MYSQL;

    @Container
    private static final JdbcDatabaseContainer<?> cockroachDb = DatabaseContainers.COCKROACHDB;

    @Container
    private static final JdbcDatabaseContainer<?> mssql = DatabaseContainers.MSSQL;

    @Container
    private static final JdbcDatabaseContainer<?> oracleFree = DatabaseContainers.ORACLE;

    protected void configure(DataSource dataSource) throws SQLException {
        this.jdbcContainer = DatabaseContainers.getContainer(dataSource);
        HikariConfig hikariConfig = DatabaseContainers.createHikariConfig(dataSource, this.jdbcContainer);
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    protected Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    protected void tearDown(DataSource dataSource, MessageQueue messageQueue) throws SQLException {
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
                case ORACLE_FREE -> {
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

    protected void assertUniquePrimaryKeyViolation(DataSource dataSource, SQLException exception) {
        switch (dataSource) {
            case H2 -> RelationalTestUtil.assertH2PrimaryKeyViolation(exception);
            case MYSQL -> RelationalTestUtil.assertMySQLPrimaryKeyViolation(exception);
            case MSSQL -> RelationalTestUtil.assertMsSQLPrimaryKeyViolation(exception);
            case COCKROACHDB, POSTGRES -> RelationalTestUtil.assertPostgresPrimaryKeyViolation(exception);
            case ORACLE_FREE -> RelationalTestUtil.assertOracleDbPrimaryKeyViolation(exception);
            default -> fail("Not implemented: %s".formatted(dataSource.name()));
        }
    }
}
