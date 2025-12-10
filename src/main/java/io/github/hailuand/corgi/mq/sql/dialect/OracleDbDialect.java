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

public class OracleDbDialect implements SqlDialect {
    @Override
    public String schemaDdl(String schemaName) {
        // Oracle creates schema based on user, so no schema creation needed
        return "SELECT 1 FROM DUAL";
    }

    @Override
    public String tableDdl(String schemaName, String tableName) {
        return """
            DECLARE
                v_count NUMBER;
            BEGIN
                SELECT COUNT(*) INTO v_count
                FROM user_tables
                WHERE table_name = '%s';

                IF v_count = 0 THEN
                    EXECUTE IMMEDIATE 'CREATE TABLE "%s" ("read_count" NUMBER DEFAULT 0 NOT NULL, "message_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, "processing_time" TIMESTAMP, "id" VARCHAR2(36) PRIMARY KEY, "read_by" VARCHAR2(16), "data" CLOB NOT NULL)';
                END IF;
            END;
            """.formatted(tableName, tableName);
    }

    @Override
    public String indexDdl(String schemaName, String tableName) {
        return """
                DECLARE
                    v_count NUMBER;
                BEGIN
                    SELECT COUNT(*) INTO v_count
                    FROM user_indexes
                    WHERE index_name = '%s';

                    IF v_count = 0 THEN
                        EXECUTE IMMEDIATE 'CREATE INDEX "%s" ON "%s" ("processing_time")';
                    END IF;
                END;
                """.formatted(getProcessingTimeIndexName(tableName), getProcessingTimeIndexName(tableName), tableName);
    }

    @Override
    public String checkIndexExistenceDql(String schemaName, String tableName) {
        return """
                SELECT COUNT(*) as count
                FROM user_indexes
                WHERE table_name = '%s'
                AND index_name = '%s'
                """.formatted(tableName, getProcessingTimeIndexName(tableName));
    }

    @Override
    public String pushMessagesDml(String schemaName, String tableName) {
        return """
                INSERT INTO "%s" ("id", "data")
                VALUES (?, ?)
                """.formatted(tableName);
    }

    @Override
    public String popMessagesDml(String schemaName, String tableName) {
        return """
                UPDATE "%s" SET "processing_time" = CURRENT_TIMESTAMP
                WHERE "id" = ?
                AND "processing_time" IS NULL
                """.formatted(tableName);
    }

    @Override
    public String updateReadCountDml(String schemaName, String tableName) {
        return """
                UPDATE "%s"
                SET "read_count" = "read_count" + 1, "read_by" = USER
                WHERE "id" = ?
                """.formatted(tableName);
    }

    @Override
    public String readMessagesDql(String schemaName, String tableName, int numMessages) {
        return """
                SELECT * FROM "%s"
                WHERE "processing_time" IS NULL
                ORDER BY "message_time" ASC
                FETCH FIRST %d ROWS ONLY
                """.formatted(tableName, numMessages);
    }
}
