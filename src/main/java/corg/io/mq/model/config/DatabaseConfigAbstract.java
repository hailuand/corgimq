package corg.io.mq.model.config;

import java.util.concurrent.TimeUnit;
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
    String password();

    @Value.Default
    default int maxConnectionPoolSize() {
        return 10;
    }

    @Value.Default
    default long connectionMaxLifetime() {
        return TimeUnit.MINUTES.toMillis(30);
    }
}
