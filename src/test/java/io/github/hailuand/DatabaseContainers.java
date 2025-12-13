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

import com.zaxxer.hikari.HikariConfig;
import java.util.concurrent.TimeUnit;
import org.testcontainers.cockroachdb.CockroachContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.mssqlserver.MSSQLServerContainer;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class DatabaseContainers {
    private static final String H2_JDBC_URL =
            "jdbc:h2:mem:TEST_DB;DATABASE_TO_UPPER=false;BUILTIN_ALIAS_OVERRIDE=TRUE;DB_CLOSE_DELAY=-1";
    private static final String H2_USER_NAME = "sa";
    private static final String H2_PASSWORD = "";

    private DatabaseContainers() {}

    public static final JdbcDatabaseContainer<?> POSTGRES =
            new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("16.2"));

    public static final JdbcDatabaseContainer<?> MYSQL = new MySQLContainer(
                    DockerImageName.parse("mysql").withTag("8.3.0"))
            .withUsername("root")
            .withPassword("")
            .withEnv("MYSQL_ROOT_HOST", "%");

    public static final JdbcDatabaseContainer<?> COCKROACHDB = new CockroachContainer(
            DockerImageName.parse("cockroachdb/cockroach").withTag("v23.1.16"));

    public static final JdbcDatabaseContainer<?> MSSQL = new MSSQLServerContainer(
                    DockerImageName.parse("mcr.microsoft.com/mssql/server").withTag("2022-CU10-ubuntu-22.04"))
            .acceptLicense();

    public static final JdbcDatabaseContainer<?> ORACLE =
            new OracleContainer(DockerImageName.parse("gvenzl/oracle-free").withTag("23.4-slim-faststart"));

    public static JdbcDatabaseContainer<?> getContainer(DataSource dataSource) {
        return switch (dataSource) {
            case H2 -> null;
            case COCKROACHDB -> COCKROACHDB;
            case POSTGRES -> POSTGRES;
            case MYSQL -> MYSQL;
            case MSSQL -> MSSQL;
            case ORACLE_FREE -> ORACLE;
        };
    }

    public static HikariConfig createHikariConfig(DataSource dataSource, JdbcDatabaseContainer<?> container) {
        String userName, password, jdbcUrl;
        if (dataSource == DataSource.H2) {
            userName = H2_USER_NAME;
            password = H2_PASSWORD;
            jdbcUrl = H2_JDBC_URL;
        } else {
            jdbcUrl = container.getJdbcUrl();
            userName = container.getUsername();
            password = container.getPassword();

            switch (dataSource) {
                case MYSQL -> jdbcUrl = "%s?sessionVariables=sql_mode=ANSI_QUOTES".formatted(jdbcUrl);
                case MSSQL -> jdbcUrl = "%s;quotedIdentifier=on".formatted(jdbcUrl);
            }
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        hikariConfig.setPoolName("CorgiMQ Test Pool");
        hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(2));

        return hikariConfig;
    }
}
