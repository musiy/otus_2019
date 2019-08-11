package ru.otus.dao;

import java.util.List;

public interface DbService {

    <T> List<T> listAll(Class<T> clazz);

    void saveOrUpdate(Object obj);

    <T> T get(Class<T> dataClass, long id);
}
