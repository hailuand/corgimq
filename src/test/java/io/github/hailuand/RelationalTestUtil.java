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

package io.github.hailuand;

import static org.junit.jupiter.api.Assertions.*;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.h2.api.ErrorCode;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public final class RelationalTestUtil {
    private static final String MSSQL_INTEGRITY_VIOLATION_SQL_STATE = "23000";
    private static final int MSSQL_INTEGRITY_VIOLATION_ERROR_CODE = 2627;

    public static void assertH2PrimaryKeyViolation(SQLException exception) {
        assertNotNull(exception.getNextException());
        var h2Exception = exception.getNextException();
        assertInstanceOf(JdbcSQLIntegrityConstraintViolationException.class, h2Exception);
        assertEquals(ErrorCode.DUPLICATE_KEY_1, Integer.parseInt(h2Exception.getSQLState()), "unique violation");
    }

    public static void assertMySQLPrimaryKeyViolation(SQLException sqlException) {
        assertInstanceOf(BatchUpdateException.class, sqlException);
        assertEquals(MysqlErrorNumbers.SQLSTATE_INTEGRITY_CONSTRAINT_VIOLATION_NO_SUBCLASS, sqlException.getSQLState());
        assertNotNull(sqlException.getCause());
        assertInstanceOf(SQLIntegrityConstraintViolationException.class, sqlException.getCause());
    }

    public static void assertMsSQLPrimaryKeyViolation(SQLException sqlException) {
        assertInstanceOf(BatchUpdateException.class, sqlException);
        assertEquals(MSSQL_INTEGRITY_VIOLATION_SQL_STATE, sqlException.getSQLState(), "PKey violation error state");
        assertEquals(MSSQL_INTEGRITY_VIOLATION_ERROR_CODE, sqlException.getErrorCode(), "PKey violation error code");
        assertTrue(sqlException.getMessage().contains("Violation of PRIMARY KEY constraint"));
        assertTrue(sqlException.getMessage().contains("Cannot insert duplicate key"));
    }

    public static void assertPostgresPrimaryKeyViolation(SQLException sqlException) {
        assertNotNull(sqlException.getNextException());
        var psqlException = sqlException.getNextException();
        assertInstanceOf(PSQLException.class, psqlException);
        assertEquals(PSQLState.UNIQUE_VIOLATION.getState(), psqlException.getSQLState());
    }
}
