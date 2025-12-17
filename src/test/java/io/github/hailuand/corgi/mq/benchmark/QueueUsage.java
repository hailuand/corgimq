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

package io.github.hailuand.corgi.mq.benchmark;

import com.zaxxer.hikari.HikariDataSource;
import io.github.hailuand.DataSource;
import io.github.hailuand.DatabaseContainers;
import io.github.hailuand.corgi.mq.MessageQueue;
import io.github.hailuand.corgi.mq.handler.MessageHandler;
import io.github.hailuand.corgi.mq.model.config.MessageHandlerConfig;
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import io.github.hailuand.corgi.mq.model.message.MessageHandlerBatch;
import io.github.hailuand.corgi.mq.sql.dialect.SqlDialectFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.testcontainers.containers.JdbcDatabaseContainer;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 3)
@Fork(1)
@State(Scope.Benchmark)
public class QueueUsage {
    @Param({"H2", "COCKROACHDB", "MYSQL", "MSSQL", "ORACLE_FREE", "POSTGRES"})
    private DataSource dataSource;

    @Param({"1", "10"})
    private int batchSize;

    @Param({"6"})
    private int threads;

    private HikariDataSource pool;
    private Connection conn;
    private MessageQueue queue;
    private MessageHandler messageHandler;

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
    public void setupIteration() throws SQLException {
        var messages = BenchmarkUtility.createMessages(10_000);
        messageHandler = MessageHandler.of(queue, MessageHandlerConfig.of(batchSize));
        queue.push(messages, conn);
    }

    @TearDown(Level.Iteration)
    public void tearDownIteration() throws SQLException {
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
    @Threads(Threads.MAX)
    public void listen() throws SQLException {
        messageHandler.listen(
                (() -> {
                    try {
                        return pool.getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }),
                MessageHandlerBatch::messages); // Mark all as read
    }
}
