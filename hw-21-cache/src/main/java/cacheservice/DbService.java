package cacheservice;

import org.hibernate.Session;

public interface DbService {

    void saveOrUpdate(Session session, Object obj);

    <T> T get(Session session, Class<T> dataClass, long id);
}
