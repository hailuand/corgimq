package corg.io.mq.model.config;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true)
public interface MessageHandlerConfigAbstract {
    @Value.Parameter
    @Value.Default
    default int maxNumMessages() {
        return 10;
    }
}
