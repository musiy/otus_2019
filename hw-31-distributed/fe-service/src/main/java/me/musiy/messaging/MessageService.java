package me.musiy.messaging;

import me.Address;
import me.Message;

import java.util.function.Consumer;

public interface MessageService {

    Address getAddress();

    void sendMessage(Message message);

    default void addHandler(Consumer<Message> handler) {
    }
}
