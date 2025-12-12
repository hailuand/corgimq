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

import io.github.hailuand.DataSource;
import io.github.hailuand.DatabaseContainers;
import io.github.hailuand.DbmsTest;
import io.github.hailuand.corgi.mq.MessageQueue;
import io.github.hailuand.corgi.mq.model.config.MessageQueueConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import io.github.hailuand.corgi.mq.sql.dialect.SqlDialectFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.datafaker.Faker;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 10)
@Fork(1)
@State(Scope.Benchmark)
public class MessageQueueThroughputBenchmark extends DbmsTest {
    private static final Faker faker = new Faker();

    @Param({"H2", "COCKROACHDB", "MYSQL", "MSSQL", "ORACLE_FREE", "POSTGRES"})
    private DataSource dataSource;

    @Param({"100000", "1000000"})
    private int batchSize;

    private Connection conn;
    private MessageQueue queue;
    private List<Message> messages;

    @Setup(Level.Trial)
    public void setupTrial() throws SQLException {
        if (dataSource != DataSource.H2) {
            var jdbcContainer = Objects.requireNonNull(DatabaseContainers.getContainer(dataSource));
            jdbcContainer.start();
        }
        configure(dataSource);
        conn = getConnection();
        queue = MessageQueue.of(
                MessageQueueConfig.of("Benchmark_%s".formatted(faker.random().hex())), conn);
        queue.createTableWithSchemaIfNotExists(conn);
    }

    @Setup(Level.Iteration)
    public void setupIteration() {
        messages = createMessages(batchSize);
    }

    @TearDown(Level.Invocation)
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
    }

    @Benchmark
    public void benchmarkPushMessages() throws SQLException {
        queue.push(messages, conn);
    }

    private List<Message> createMessages(int count) {
        var messages = new ArrayList<Message>();
        for (int i = 0; i < count; i++) {
            messages.add(Message.of(faker.json()));
        }
        return messages;
    }
}
