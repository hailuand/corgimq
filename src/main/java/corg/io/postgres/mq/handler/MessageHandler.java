package corg.io.postgres.mq.handler;

import corg.io.postgres.mq.model.message.Message;

import java.util.List;

public interface MessageHandler {
    List<Message> handle(List<Message> messages);
}
