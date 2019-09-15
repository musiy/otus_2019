package me;

import java.util.UUID;

public class Message {

    /** Уникальный идентификатор сообщения */
    private String uuid;

    /**
     * Имя класса, содержащего сообщение
     */
    private String className;

    /** Компонент-отправитель сообщения */
    private Address from;

    /** Компонент-получатель сообщения */
    private Address to;

    public Message() {

    }

    public Message(Address from, Address to) {
        this.uuid = UUID.randomUUID().toString();
        this.className = this.getClass().getName();
        this.from = from;
        this.to = to;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }
}
