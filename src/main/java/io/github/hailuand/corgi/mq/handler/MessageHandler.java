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

package io.github.hailuand.corgi.mq.handler;

import io.github.hailuand.corgi.mq.MessageQueue;
import io.github.hailuand.corgi.mq.TransactionManager;
import io.github.hailuand.corgi.mq.model.config.MessageHandlerConfig;
import io.github.hailuand.corgi.mq.model.message.Message;
import io.github.hailuand.corgi.mq.model.message.MessageHandlerBatch;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final MessageQueue messageQueue;
    private final MessageHandlerConfig messageHandlerConfig;
    private final TransactionManager transactionManager;

    public static MessageHandler of(MessageQueue messageQueue) {
        return of(messageQueue, MessageHandlerConfig.builder().build());
    }

    public static MessageHandler of(MessageQueue messageQueue, MessageHandlerConfig messageHandlerConfig) {
        return new MessageHandler(messageQueue, messageHandlerConfig);
    }

    private MessageHandler(MessageQueue messageQueue, MessageHandlerConfig messageHandlerConfig) {
        this.messageQueue = Objects.requireNonNull(messageQueue);
        this.messageHandlerConfig = Objects.requireNonNull(messageHandlerConfig);
        this.transactionManager = new TransactionManager();
    }

    public void listen(Supplier<Connection> connectionProvider, Function<MessageHandlerBatch, List<Message>> handler)
            throws SQLException {
        this.transactionManager.executeInTransaction(connectionProvider, transactionConnection -> {
            try {
                var messages =
                        this.messageQueue.read(this.messageHandlerConfig.messageBatchSize(), transactionConnection);
                if (!messages.isEmpty()) {
                    var handled = handler.apply(MessageHandlerBatch.of(messages, transactionConnection));
                    this.messageQueue.pop(handled, transactionConnection);
                } else {
                    logger.debug("No messages returned to handle");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
