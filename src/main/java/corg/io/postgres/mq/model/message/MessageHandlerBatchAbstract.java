package corg.io.postgres.mq.model.message;

import org.immutables.value.Value;

import java.sql.Connection;
import java.util.List;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true
)
public interface MessageHandlerBatchAbstract {
    @Value.Parameter
    List<Message> messages();

    @Value.Parameter
    Connection transactionConnection();
}
