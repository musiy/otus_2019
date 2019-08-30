package me.musiy.messaging.messages;

import me.musiy.dao.DbService;
import me.musiy.messaging.Address;
import me.musiy.messaging.Addressee;

public abstract class MsgToDb extends Message {

    public MsgToDb(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DbService) {
            exec((DbService) addressee);
        }
    }

    public abstract void exec(DbService dbService);
}
