package me.messages;

import me.Address;
import me.Message;

public class InternalErrorMessage extends Message {

    public InternalErrorMessage(Address from, Address to) {
        super(from, to);
    }
}
