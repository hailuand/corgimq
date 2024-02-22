package corg.io.postgres.mq.model.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true
)
@JsonSerialize(as = Message.class)
@JsonDeserialize(as = Message.class)
public interface MessageAbstract {
    @Value.Default
    default String id() {
        return UUID.randomUUID().toString();
    }

    @Value.Parameter
    String data();
}
