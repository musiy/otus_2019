package me.musiy.messaging;

import me.Address;
import me.Message;
import me.engine.ClientMessageQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

@Service
public class FeMessageQueueService extends ClientMessageQueue implements MessageService {

    private Consumer<Message> handler;

    public FeMessageQueueService(@Value("${message-server.port}") int serverPort) {
        super(Address.Type.FE, serverPort);
    }

    @PostConstruct
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void addHandler(Consumer<Message> handler) {
        this.handler = handler;
    }

    @Override
    public void handleMessage(Message message) {
        handler.accept(message);
    }
}
