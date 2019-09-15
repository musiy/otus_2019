package me.musiy.messaging;

import me.Address;
import me.Message;
import me.engine.ClientMessageQueue;
import me.messages.NewEntityMessage;
import me.messages.SaveEntityMessage;
import me.musiy.dao.DbService;
import me.musiy.services.DtoFactory;
import me.musiy.userspace.DtoEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DbMessageQueueService extends ClientMessageQueue implements MessageService {

    private DtoFactory dtoFactory;

    private DbService dbService;

    public DbMessageQueueService(@Value("${message-server.port}") int serverPort,
                                 DtoFactory dtoFactory,
                                 DbService dbService) {
        super(Address.Type.DB, serverPort);
        this.dtoFactory = dtoFactory;
        this.dbService = dbService;
    }

    @PostConstruct
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void handleMessage(Message message) {
        if (message instanceof SaveEntityMessage) {
            SaveEntityMessage sem = (SaveEntityMessage) message;
            DtoEntity entity = dtoFactory.entity(sem.getEntityName(), sem.getProps());
            dbService.saveOrUpdate(entity);
            NewEntityMessage reply = new NewEntityMessage(getAddress(), message.getFrom(), sem.getProps(), sem.getEntityName());
            reply.getProps().put("id", String.valueOf(entity.getId()));
            sendMessage(reply);
        }
    }
}
