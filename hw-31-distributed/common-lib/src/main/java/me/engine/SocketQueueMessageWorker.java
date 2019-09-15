package me.engine;

import com.google.gson.Gson;
import me.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Брокер обмена сообщениями сокета. Основной компонент брокера - открытый сокет и два потока,
 * один из которых читает из сокета, а второй пишет в сокет.
 */
public class SocketQueueMessageWorker implements QueueMessageWorker, AutoCloseable {

    private final ExecutorService executorService;
    private final Socket socket;

    // очередь исходящих сообщений
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>();
    // очередь входящих сообщений
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>();

    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    public SocketQueueMessageWorker(Socket socket, BufferedReader socketReader) {
        this(socket, socketReader, null);
    }

    public SocketQueueMessageWorker(Socket socket, PrintWriter socketWriter) {
        this(socket, null, socketWriter);
    }

    private SocketQueueMessageWorker(Socket socket, BufferedReader socketReader, PrintWriter socketWriter) {
        this.socket = socket;
        this.socketReader = socketReader;
        this.socketWriter = socketWriter;
        // executor service для обработки двух очередей - входящей и исходящей
        executorService = Executors.newFixedThreadPool(2);
    }

    public void init() throws IOException {
        if (socketReader == null) {
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        if (socketWriter == null) {
            socketWriter = new PrintWriter(socket.getOutputStream(), true);
        }
        executorService.execute(this::sendMessageWorker);
        executorService.execute(this::receiveMessageWorker);
    }

    @Override
    public Message pool() {
        return input.poll();
    }

    @Override
    public void send(Message message) {
        output.add(message);
    }

    public Message take() {
        try {
            return input.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        socketReader.close();
        socketWriter.close();
        socket.close();
        executorService.shutdown();
    }

    /**
     * Ожидает на очереди исходящиз сообеший и при появлении - отправляет сообщение в сокет
     */
    private void sendMessageWorker() {
        try {
            while (socket.isConnected()) {
                Message message = output.take();
                String json = new Gson().toJson(message);
                // отправляем сообщения в сокет по одному
                socketWriter.println(json);
                // каждое сообщение разделено символом перевода строки
                socketWriter.println();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при записи исходящего сообщения в очредь", e);
        }
    }

    /**
     * Читает входящее сообщение из сокета и складывает их в очередь
     */
    private void receiveMessageWorker() {
        try {
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = socketReader.readLine()) != null) {
                sb.append(inputLine);
                if (inputLine.isEmpty()) {
                    // если встретили пустую строку - конвертируем сообщение в очередь
                    String json = sb.toString();
                    sb = new StringBuilder();
                    Message message = new Gson().fromJson(json, Message.class);
                    Class<?> aClass;
                    try {
                        aClass = Class.forName(message.getClassName());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        continue;
                    }
                    Object m = new Gson().fromJson(json, aClass);
                    input.add((Message) m);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении и обработке входящего сообщения", e);
        }
    }
}
