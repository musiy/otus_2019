package me.musiy.controllers;

import me.musiy.controllers.messages.Notification;
import me.musiy.controllers.messages.Request;
import me.musiy.controllers.messages.Response;
import me.musiy.dao.DbService;
import me.musiy.messaging.Address;
import me.musiy.messaging.MessageSystem;
import me.musiy.messaging.messages.instances.MsgSaveEntity;
import me.musiy.services.DtoFactory;
import me.musiy.userspace.DtoEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class FrontendServiceImpl implements FrontendService {

    private final Address ADDRESS = new Address("FE");

    private MessageSystem messageSystem;

    private DbService dbService;

    private DtoFactory dtoFactory;

    private SimpMessagingTemplate template;

    public FrontendServiceImpl(MessageSystem messageSystem,
                               DbService dbService,
                               DtoFactory dtoFactory,
                               SimpMessagingTemplate messagingTemplate) {
        this.messageSystem = messageSystem;
        this.dbService = dbService;
        this.dtoFactory = dtoFactory;
        this.template = messagingTemplate;
    }

    @PostConstruct
    public void init() {
        messageSystem.addAddressee(ADDRESS, this);
    }

    @MessageMapping("/message")
    @SendTo("/topic/response")
    @Override
    public Response handleRequest(Request request) {
        DtoEntity entity = dtoFactory.entity(request.getEntity(), request.getContent());
        switch (request.getCommand()) {
            case "create":
                messageSystem.sendMessage(
                        new MsgSaveEntity(messageSystem, this.getAddress(), dbService.getAddress(),
                                entity, request.getRequestId()));
                return new Response(0, "ok");
            default:
                return new Response(1, "unknown command");
        }
    }

    @Override
    public void handleMessage(Notification notification) {
        sentToClient(notification);
    }

    public void sentToClient(Notification notification) {
        template.convertAndSend("/topic/notification", notification);
    }

    @Override
    public Address getAddress() {
        return ADDRESS;
    }
}
