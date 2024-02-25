package corg.io.mq.model.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true)
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
