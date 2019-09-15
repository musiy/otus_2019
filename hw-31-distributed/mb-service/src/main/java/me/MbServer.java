package me;

import me.engine.QueueMessageWorker;
import me.engine.SocketQueueMessageWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MbServer {

    private static final int PORT = 6000;
    private static final long MIRROR_DELAY_MS = 100;

    private Random random = new Random(System.currentTimeMillis());

    private static final String DB_CLIENT_START_COMMAND =
            "java -jar ../db-service/target/db-service-1.0-SNAPSHOT.jar";

    private static final String FE_CLIENT_START_COMMAND =
            "java -jar ../fe-service/target/fe-service-1.0-SNAPSHOT.jar --server.port={port}";

    private Map<Address, QueueMessageWorker> workers = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        boolean isForceStart = true;
        if (args.length > 0 && args[0].equals("DO_NOT_START_PROCESSES")) {
            isForceStart = false;
        }
        new MbServer().start();
        System.out.println("Waiting for connections...");
        ProcessRunner dbRunner1 = null;
        ProcessRunner dbRunner2 = null;
        ProcessRunner feRunner1 = null;
        ProcessRunner feRunner2 = null;
        try {
            if (isForceStart) {
                dbRunner1 = new ProcessRunnerImpl("DB1", DB_CLIENT_START_COMMAND);
                dbRunner1.start();
                dbRunner2 = new ProcessRunnerImpl("DB2", DB_CLIENT_START_COMMAND);
                dbRunner2.start();

                feRunner1 = new ProcessRunnerImpl("FE1", FE_CLIENT_START_COMMAND.replace("{port}", "9091"));
                feRunner1.start();
                feRunner2 = new ProcessRunnerImpl("FE2", FE_CLIENT_START_COMMAND.replace("{port}", "9092"));
                feRunner2.start();
            }
            Thread.currentThread().join();
        } finally {
            if (isForceStart) {
                if (dbRunner1 != null) {
                    dbRunner1.close();
                }
                if (dbRunner2 != null) {
                    dbRunner2.close();
                }
                if (feRunner1 != null) {
                    feRunner1.close();
                }
                if (feRunner2 != null) {
                    feRunner2.close();
                }
            }
        }
    }

    private void start() {
        ExecutorService incomingListener = Executors.newFixedThreadPool(1);
        // слушает входящие подключения на порт
        incomingListener.submit(this::listenIncomingConnections);
        ExecutorService dispatchService = Executors.newFixedThreadPool(1);
        dispatchService.submit(this::dispatch);
    }

    /**
     * Слушает входящие соединения на указанном порту и создаёт очередь для обмена сообщениями
     */
    private void listenIncomingConnections() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Header header = getHeader(socketReader);
                    Address address = header.getAddress();
                    System.out.println("Got connection from client: " + address);
                    SocketQueueMessageWorker messageWorker = new SocketQueueMessageWorker(socket, socketReader);
                    messageWorker.init();
                    workers.put(address, messageWorker);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Header getHeader(BufferedReader bufferedReader) throws IOException {
        String protocolName = bufferedReader.readLine();
        if (!Header.PROTOCOL_NAME.equals(protocolName)) {
            throw new IllegalArgumentException("Unknown protocol: " + protocolName);
        }
        String protocolVersion = bufferedReader.readLine();
        if (!Header.VERSION.equals(protocolVersion)) {
            throw new IllegalArgumentException("Unknown protocol version: " + protocolVersion);
        }
        String id = bufferedReader.readLine();
        int pos = id.indexOf(':');
        if (pos == -1) {
            throw new IllegalArgumentException("Client id incorrect format");
        }
        Address.Type type = Address.Type.valueOf(id.substring(0, pos));
        String clientId = id.substring(pos + 1);
        bufferedReader.readLine(); // пустая строка после заголовка
        return new Header(protocolName, protocolVersion, new Address(clientId, type));
    }


    /**
     * Обходит все воркеры и если есть сообщение - перенаправляет в нужную очередь
     */
    private void dispatch() {
        // В бесконечном цикле через неблокирующие вызовы проверяем, не было ли сообщений от клиентов
        while (true) {
            for (var entry : workers.entrySet()) {
                QueueMessageWorker queueMessageWorker = entry.getValue();
                Message message = queueMessageWorker.pool();
                if (message != null) {
                    List<Address> addressee = findAddressee(message.getTo());
                    addressee.forEach(a -> {
                        QueueMessageWorker toWorker = workers.get(a);
                        toWorker.send(message);
                    });
                }
            }
            try {
                Thread.sleep(MIRROR_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Находит адресат для сообщения.
     * Сообщения в сторону базы данных направляются одному адресату (случайно выбранному).
     * Сообщения в сторону фронта направляются всем фронтам.
     */
    private List<Address> findAddressee(Address address) {
        final List<Address> addresses = workers.keySet().stream()
                .filter(a -> a.getType() == address.getType())
                .collect(Collectors.toList());
        switch (address.getType()) {
            case DB:
                // если адресат - база данных - отправляем первой (случайной)
                int i = random.nextInt(addresses.size());
                return Collections.singletonList(addresses.get(i));
            case FE:
                return addresses;
            default:
                return Collections.emptyList();
        }
    }
}
