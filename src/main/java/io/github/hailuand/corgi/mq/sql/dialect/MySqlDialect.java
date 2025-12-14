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

public class MySqlDialect extends StandardSqlDialect {
    @Override
    public String schemaDdl(String schemaName) {
        return handleMySqlIdentifiers(super.schemaDdl(schemaName));
    }

    @Override
    public String tableDdl(String schemaName, String tableName) {
        return handleMySqlIdentifiers(super.tableDdl(schemaName, tableName));
    }

    @Override
    public String indexDdl(String schemaName, String tableName) {
        return handleMySqlIdentifiers("""
                CREATE INDEX "%s"
                ON "%s"."%s" ("processing_time", "message_time")
                """.formatted(getProcessingTimeIndexName(tableName), schemaName, tableName));
    }

    @Override
    public String checkIndexExistenceDql(String schemaName, String tableName) {
        return """
                SELECT COUNT(*) as count
                FROM "INFORMATION_SCHEMA"."STATISTICS"
                WHERE "table_schema" = '%s'
                AND "table_name" = '%s'
                AND "index_name" = '%s';
                """.formatted(schemaName, tableName, getProcessingTimeIndexName(tableName));
    }

    @Override
    public String pushMessagesDml(String schemaName, String tableName) {
        return handleMySqlIdentifiers(super.pushMessagesDml(schemaName, tableName));
    }

    @Override
    public String popMessagesDml(String schemaName, String tableName) {
        return handleMySqlIdentifiers(super.popMessagesDml(schemaName, tableName));
    }

    @Override
    public String updateReadCountDml(String schemaName, String tableName) {
        return handleMySqlIdentifiers(super.updateReadCountDml(schemaName, tableName));
    }

    @Override
    public String readMessagesDql(String schemaName, String tableName, int numMessages) {
        return handleMySqlIdentifiers(super.readMessagesDql(schemaName, tableName, numMessages));
    }

    @Override
    public String truncateTableDml(String schemaName, String tableName) {
        return handleMySqlIdentifiers(super.truncateTableDml(schemaName, tableName));
    }

    private String handleMySqlIdentifiers(String sql) {
        return sql.replace('\"', '`');
    }
}
