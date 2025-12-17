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
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
public class QueuePushConcurrency {
    @Param({"H2", "COCKROACHDB", "MYSQL", "MSSQL", "ORACLE_FREE", "POSTGRES"})
    private DataSource dataSource;

    @Param({"10", "100"})
    private int batchSize;

    @Param({"4", "12"})
    private int threads;

    private HikariDataSource pool;
    private MessageQueue queue;

    @Setup(Level.Trial)
    public void setupTrial() throws SQLException {
        JdbcDatabaseContainer<?> container = null;
        if (dataSource != DataSource.H2) {
            container = Objects.requireNonNull(DatabaseContainers.getContainer(dataSource));
            container.start();
        }
        var config = DatabaseContainers.createHikariConfig(dataSource, container);
        config.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(10));
        pool = new HikariDataSource(config);
        try (var conn = pool.getConnection()) {
            queue = MessageQueue.of(MessageQueueConfig.of(BenchmarkUtility.createQueueName("push")), conn);
            queue.createTableWithSchemaIfNotExists(conn);
        }
    }

    @Benchmark
    @Threads(Threads.MAX)
    public void push(ThreadState state) throws SQLException {
        queue.push(state.messages, state.threadConn);
    }

    @State(Scope.Thread)
    public static class ThreadState {
        Connection threadConn;
        List<Message> messages;

        @Setup(Level.Invocation)
        public void setupInvocation(QueuePushConcurrency parent) throws SQLException {
            messages = BenchmarkUtility.createMessages(parent.batchSize);
            threadConn = parent.pool.getConnection();
        }

        @TearDown(Level.Invocation)
        public void tearDownInvocation() throws SQLException {
            if (threadConn != null && !threadConn.isClosed()) {
                threadConn.close();
            }
        }
    }
}
