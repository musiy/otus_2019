package me.musiy.dao;

import me.musiy.userspace.DtoEntity;

public interface DbService {

//    <T> List<T> listAll(Class<T> clazz);

    <T extends DtoEntity> T saveOrUpdate(T obj);
//
//    <T> T get(Class<T> dataClass, long id);
}
