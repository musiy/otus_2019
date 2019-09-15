package me.engine;

import me.Address;
import me.Header;
import me.Message;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ClientMessageQueue {

    private static final long MIRROR_DELAY_MS = 100;

    private Address address;

    private int serverPort;

    private QueueMessageWorker queueMessageWorker;

    private ExecutorService dispatcher;

    public ClientMessageQueue(Address.Type componentType, int serverPort) {
        this.address = new Address(UUID.randomUUID().toString(), componentType);
        this.serverPort = serverPort;
    }

    public void init() throws Exception {
        Socket socket = new Socket("localhost", serverPort);
        PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
        socketWriter.println(Header.PROTOCOL_NAME);
        socketWriter.println(Header.VERSION);
        socketWriter.println(address.getType().toString() + ":" + address.getId());
        socketWriter.println();
        queueMessageWorker = new SocketQueueMessageWorker(socket, socketWriter);
        queueMessageWorker.init();
        dispatcher = Executors.newFixedThreadPool(1);
        dispatcher.submit(this::dispatch);
    }

    public Address getAddress() {
        return address;
    }

    public void sendMessage(Message message) {
        queueMessageWorker.send(message);
    }

    private void dispatch() {
        // В бесконечном цикле через неблокирующие вызовы проверяем, не было ли сообщений от клиентов
        while (true) {
            try {
                Message message = queueMessageWorker.pool();
                if (message != null) {
                    handleMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(MIRROR_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void handleMessage(Message message);
}
