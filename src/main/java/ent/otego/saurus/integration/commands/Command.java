package ent.otego.saurus.integration.commands;

import ent.otego.saurus.integration.ChatMessage;

public interface Command<T> {

    String getName();

    void executeCommand(ChatMessage<T> message);
}
