package corg.io.postgres.mq.model.config;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true
)
public interface DbConfigAbstract {
    @Value.Parameter
    String jdbcUrl();

    @Value.Parameter
    @Value.Redacted
    String username();

    @Value.Parameter
    @Value.Redacted
    String password();
}
