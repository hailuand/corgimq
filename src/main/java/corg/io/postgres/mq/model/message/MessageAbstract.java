package corg.io.postgres.mq.model.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

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
    @Value.Parameter
    String id();

    @Value.Parameter
    String data();
}
