package me.musiy.messaging.messages.instances;

import me.musiy.controllers.FrontendService;
import me.musiy.controllers.messages.NewEntityNotification;
import me.musiy.messaging.Address;
import me.musiy.messaging.messages.MsgToFrontend;
import me.musiy.userspace.DtoEntity;

public class MsgSendBackIdOnSave extends MsgToFrontend {

    private String reqId;

    private DtoEntity entity;

    public MsgSendBackIdOnSave(Address from, Address to, String reqId, DtoEntity entity) {
        super(from, to);
        this.reqId = reqId;
        this.entity = entity;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.handleMessage(new NewEntityNotification(reqId, entity));
    }
}
