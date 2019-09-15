package me.musiy.services;

import me.musiy.userspace.DtoEntity;

import java.util.Map;

public interface DtoFactory {

    DtoEntity entity(String name, Map<String, String> values);
}
