package me.engine;

import me.Message;

/**
 * Интерфейс очереди для обмена сообщениями
 */
public interface QueueMessageWorker {

    /**
     * Возвращает сообщение из очереди входящих сообщений.
     * Не блокирующий вызов - если сообщения нет, возвращает null.
     */
    Message pool();

    /**
     * Возвращает сообщение из очереди входящих сообщений.
     * Блокируется на очереди сообщений до тех пор, пока в ней не появится сообщение.
     */
    Message take();

    /**
     * Отправить сообщение через воркер
     */
    void send(Message message);

    void init() throws Exception;

}