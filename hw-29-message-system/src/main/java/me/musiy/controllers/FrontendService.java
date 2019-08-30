package me.musiy.controllers;

import me.musiy.controllers.messages.Notification;
import me.musiy.controllers.messages.Request;
import me.musiy.controllers.messages.Response;
import me.musiy.messaging.Addressee;

public interface FrontendService extends Addressee {

    Response handleRequest(Request request);

    void handleMessage(Notification notification);
}

