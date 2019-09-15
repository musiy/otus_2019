package me.messages;

import me.Address;

public class AddresseeNotFound extends InternalErrorMessage {

    public AddresseeNotFound(Address from, Address to) {
        super(from, to);
    }
}
