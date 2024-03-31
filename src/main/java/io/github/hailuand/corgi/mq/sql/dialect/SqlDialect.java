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

public interface SqlDialect {
    String schemaDdl(String schemaName);

    String tableDdl(String schemaName, String tableName);

    String indexDdl(String schemaName, String tableName);

    String checkIndexExistenceDql(String schemaName, String tableName);

    String pushMessagesDml(String schemaName, String tableName);

    String popMessagesDml(String schemaName, String tableName);

    String updateReadCountDml(String schemaName, String tableName);

    String readMessagesDql(String schemaName, String tableName, int numMessages);
}
