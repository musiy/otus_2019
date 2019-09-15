package me.musiy.controllers;

import me.musiy.controllers.messages.Notification;

public interface ControllerService {

    void sentToClient(Notification notification);
}
