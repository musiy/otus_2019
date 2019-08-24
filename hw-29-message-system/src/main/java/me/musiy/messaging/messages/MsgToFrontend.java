package me.musiy.messaging.messages;

import me.musiy.controllers.FrontendService;
import me.musiy.messaging.Address;
import me.musiy.messaging.Addressee;

public abstract class MsgToFrontend extends Message {

    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        }
    }

    public abstract void exec(FrontendService frontendService);
}