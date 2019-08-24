package me.musiy.controllers.messages;

public class Response {

    private int code;

    private String content;

    public Response(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }
}
