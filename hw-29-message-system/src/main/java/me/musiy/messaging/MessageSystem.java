package me.musiy.messaging;

import me.musiy.messaging.messages.Message;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MessageSystem {

    private final static Logger LOGGER = Logger.getLogger(MessageSystem.class.getName());

    private final List<Thread> workers = new ArrayList<>();
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap = new HashMap<>();

    public void addAddressee(Address address, Addressee addressee) {
        messagesMap.put(address, new LinkedBlockingQueue<>());
        runListenThread(address, addressee);
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }

    private void runListenThread(Address address, Addressee addressee) {
        String name = "MS-worker-" + address.getId();
        Thread thread = new Thread(() -> {
            while (true) {
                LinkedBlockingQueue<Message> queue = messagesMap.get(address);
                while (true) {
                    try {
                        Message message = queue.take();
                        message.exec(addressee);
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                        return;
                    }
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.add(thread);
    }

    @PreDestroy
    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
