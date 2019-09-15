package me.musiy.messaging;

import me.Address;
import me.Message;

public interface MessageService {

    Address getAddress();

    void sendMessage(Message message);
}
