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

package io.github.hailuand.corgi.mq.benchmark.throughput;

import com.zaxxer.hikari.HikariDataSource;
import io.github.hailuand.DataSource;
import io.github.hailuand.DatabaseContainers;
import io.github.hailuand.corgi.mq.MessageQueue;
import io.github.hailuand.corgi.mq.benchmark.BenchmarkUtility;
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import io.github.hailuand.corgi.mq.sql.dialect.SqlDialectFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.testcontainers.containers.JdbcDatabaseContainer;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 10)
@Fork(1)
@State(Scope.Benchmark)
public class MessageQueuePopThroughputBenchmark {
    @Param({"H2", "COCKROACHDB", "MYSQL", "MSSQL", "ORACLE_FREE", "POSTGRES"})
    private DataSource dataSource;

    @Param({"100000", "1000000"})
    private int batchSize;

    private HikariDataSource pool;
    private Connection conn;
    private MessageQueue queue;
    private List<Message> messages;

    @Setup(Level.Trial)
    public void setupTrial() throws SQLException {
        JdbcDatabaseContainer<?> container = null;
        if (dataSource != DataSource.H2) {
            container = Objects.requireNonNull(DatabaseContainers.getContainer(dataSource));
            container.start();
        }
        var config = DatabaseContainers.createHikariConfig(dataSource, container);
        config.setLeakDetectionThreshold(TimeUnit.MINUTES.toMillis(5));
        pool = new HikariDataSource(config);
        conn = pool.getConnection();

        queue = MessageQueue.of(MessageQueueConfig.of(BenchmarkUtility.createQueueName("pop")), conn);
        queue.createTableWithSchemaIfNotExists(conn);
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        messages = BenchmarkUtility.createMessages(batchSize);
    }

    @Setup(Level.Invocation)
    public void setupInvocation() throws SQLException {
        queue.push(messages, conn);
    }

    @TearDown(Level.Invocation)
    public void tearDownInvocation() throws SQLException {
        var dialect = SqlDialectFactory.fromConnection(conn);
        try (PreparedStatement stmt =
                conn.prepareStatement(dialect.truncateTableDml(queue.tableSchemaName(), queue.queueTableName()))) {
            stmt.execute();
        }
    }

    @TearDown(Level.Trial)
    public void tearDownTrial() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
    }

    @Benchmark
    public void benchmarkPop() throws SQLException {
        queue.pop(messages, conn);
    }
}
