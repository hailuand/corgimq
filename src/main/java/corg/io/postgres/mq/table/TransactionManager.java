package corg.io.postgres.mq.table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TransactionManager {
    public void executeInTransaction(Supplier<Connection> supplier, Consumer<Connection> consumer) throws SQLException {
        try(var conn = supplier.get()) {
            conn.setAutoCommit(false);
            try {
                consumer.accept(conn);
                conn.commit();
            }
            catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        }
    }
}
