package me.musiy.controllers.messages;

public class NewEntityNotification extends Notification {

    private Object entity;

    private String type;

    public NewEntityNotification(String requestId, Object entity) {
        super(requestId);
        this.entity = entity;
        this.type = "entity";
    }

    public Object getEntity() {
        return entity;
    }

    public String getType() {
        return type;
    }
}
