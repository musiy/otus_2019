package me.musiy.controllers.messages;

public class Notification {

    private String requestId;

    public Notification(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
