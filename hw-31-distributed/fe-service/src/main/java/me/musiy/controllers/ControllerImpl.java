package me.musiy.controllers;

import me.Address;
import me.Message;
import me.messages.NewEntityMessage;
import me.messages.SaveEntityMessage;
import me.musiy.controllers.messages.NewEntityNotification;
import me.musiy.controllers.messages.Notification;
import me.musiy.controllers.messages.Request;
import me.musiy.controllers.messages.Response;
import me.musiy.messaging.FeMessageQueueService;
import me.musiy.messaging.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@Controller
public class ControllerImpl implements ControllerService {

    private final SimpMessagingTemplate template;

    private final MessageService messageQueueService;

    public ControllerImpl(SimpMessagingTemplate template,
                          FeMessageQueueService messageQueueService) {
        this.template = template;
        this.messageQueueService = messageQueueService;
    }

    private void handle(Message m) {
        if (m instanceof NewEntityMessage) {
            NewEntityMessage message = (NewEntityMessage) m;
            Map<String, String> entity = message.getProps();
            entity.put("name", entity.get("fio"));
            NewEntityNotification notification = new NewEntityNotification(UUID.randomUUID().toString(), entity);
            sentToClient(notification);
        } else {
            System.out.println("Unknown message: " + m);
        }
    }

    @PostConstruct
    public void init() {
        messageQueueService.addHandler(this::handle);
    }

    @MessageMapping("/message")
    @SendTo("/topic/response")
    public Response handleRequest(Request request) {
        switch (request.getCommand()) {
            case "create":
                Message message = new SaveEntityMessage(messageQueueService.getAddress(),
                        new Address(null, Address.Type.DB), request.getContent(), request.getEntity());
                messageQueueService.sendMessage(message);
                return new Response(0, "ok");
            default:
                return new Response(1, "unknown command");
        }
    }

    @Override
    public void sentToClient(Notification notification) {
        template.convertAndSend("/topic/notification", notification);
    }
}
