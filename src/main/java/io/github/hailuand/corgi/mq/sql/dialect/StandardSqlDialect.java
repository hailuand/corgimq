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

package io.github.hailuand.corgi.mq.sql.dialect;

public class StandardSqlDialect implements SqlDialect {
    @Override
    public String schemaDdl(String schemaName) {
        return """
                CREATE SCHEMA IF NOT EXISTS "%s";
                """.formatted(schemaName);
    }

    @Override
    public String tableDdl(String schemaName, String tableName) {
        return """
                CREATE TABLE IF NOT EXISTS "%s"."%s" (
                    "read_count" INTEGER DEFAULT 0 NOT NULL,
                    "message_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                    "processing_time" TIMESTAMP,
                    "id" VARCHAR(36) PRIMARY KEY,
                    "read_by" VARCHAR(16),
                    "data" TEXT NOT NULL
                );
                """.formatted(schemaName, tableName);
    }

    @Override
    public String indexDdl(String schemaName, String tableName) {
        return """
                CREATE INDEX IF NOT EXISTS "%s"
                ON "%s"."%s" ("processing_time")
                """.formatted(getProcessingTimeIndexName(tableName), schemaName, tableName);
    }

    @Override
    public String checkIndexExistenceDql(String schemaName, String tableName) {
        return "SELECT COUNT(*) as count WHERE 1 = 0"; // CREATE INDEX IF EXISTS used, so always safe to run
    }

    @Override
    public String pushMessagesDml(String schemaName, String tableName) {
        return """
                INSERT INTO "%s"."%s" ("id", "data")
                VALUES (?, ?)
                """.formatted(schemaName, tableName);
    }

    @Override
    public String popMessagesDml(String schemaName, String tableName) {
        return """
                    UPDATE "%s"."%s" SET "processing_time" = CURRENT_TIMESTAMP
                    WHERE "id" = ?
                    AND "processing_time" IS NULL
                    """.formatted(schemaName, tableName);
    }

    @Override
    public String updateReadCountDml(String schemaName, String tableName) {
        return """
                UPDATE "%s"."%s"
                SET "read_count" = "read_count" + 1, "read_by" = CURRENT_USER
                WHERE "id" = ?
                """.formatted(schemaName, tableName);
    }

    @Override
    public String readMessagesDql(String schemaName, String tableName, int numMessages) {
        return """
                SELECT * FROM "%s"."%s"
                WHERE "processing_time" IS NULL
                ORDER BY "message_time" ASC
                LIMIT %d
                FOR UPDATE SKIP LOCKED
                """.formatted(schemaName, tableName, numMessages);
    }

    protected String getProcessingTimeIndexName(String tableName) {
        return "%s_processing_time_idx".formatted(tableName);
    }
}
