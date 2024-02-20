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
public interface MessageQueueConfigAbstract {
    @Value.Parameter
    String queueName();
}