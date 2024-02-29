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

package corg.io.mq.model.config;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true)
public interface DatabaseConfigAbstract {
    @Value.Parameter
    String jdbcUrl();

    @Value.Parameter
    @Value.Redacted
    String username();

    @Value.Parameter
    @Value.Redacted
    Supplier<String> passwordProvider();

    @Value.Default
    default int maxConnectionPoolSize() {
        return 10;
    }

    @Value.Default
    default long connectionMaxLifetime() {
        return TimeUnit.MINUTES.toMillis(30);
    }

    @Value.Default
    default Map<String, String> additionalDataSourceProperties() {
        return Collections.emptyMap();
    }
}
