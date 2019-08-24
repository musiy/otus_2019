package me.musiy.messaging.messages.instances;

import me.musiy.dao.DbService;
import me.musiy.messaging.Address;
import me.musiy.messaging.MessageSystem;
import me.musiy.messaging.messages.MsgToDb;
import me.musiy.userspace.DtoEntity;

public class MsgSaveEntity extends MsgToDb {

    private MessageSystem messageSystem;

    private DtoEntity entity;

    private String reqId;

    public MsgSaveEntity(MessageSystem messageSystem, Address from, Address to, DtoEntity entity, String reqId) {
        super(from, to);
        this.messageSystem = messageSystem;
        this.entity = entity;
        this.reqId = reqId;
    }

    @Override
    public void exec(DbService dbService) {
        DtoEntity entity = dbService.saveOrUpdate(this.entity);
        messageSystem.sendMessage(new MsgSendBackIdOnSave(getTo(), getFrom(), reqId, entity));
    }
}
