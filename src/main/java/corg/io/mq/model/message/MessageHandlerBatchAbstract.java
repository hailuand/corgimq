package corg.io.mq.model.message;

import java.sql.Connection;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        typeAbstract = "*Abstract",
        typeImmutable = "*",
        jdkOnly = true,
        optionalAcceptNullable = true,
        strictBuilder = true)
public interface MessageHandlerBatchAbstract {
    @Value.Parameter
    List<Message> messages();

    @Value.Parameter
    Connection transactionConnection();
}
