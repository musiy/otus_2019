package ru.otus.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import ru.otus.cache.Cache;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class DbServiceImpl implements DbService {

    private final Cache<String, Object> cache;

    private final SessionFactory sessionFactory;

    public DbServiceImpl(Cache<String, Object> cache, SessionFactory sessionFactory) {
        this.cache = cache;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <T> List<T> listAll(Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(clazz);
            Root<T> root = query.from(clazz);
            query.select(root);
            Query<T> q = session.createQuery(query);
            return q.getResultList();
        }
    }

    public void saveOrUpdate(Object obj) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(obj);
            tx.commit();
            String cacheKey = getCacheKey(obj.getClass(), getId(obj));
            cache.put(cacheKey, obj);
        }
    }

    @Override
    public <T> T get(Class<T> clazz, long id) {
        try (Session session = sessionFactory.openSession()) {
            String cacheKey = getCacheKey(clazz, id);
            Object o = cache.get(cacheKey);
            if (o != null) {
                return (T) o;
            }
            return session.get(clazz, id);
        }
    }

    private String getCacheKey(Class clazz, Long id) {
        return clazz.getName() + "_" + id;
    }

    private Long getId(Object obj) {
        try {
            Method method = obj.getClass().getMethod("getId");
            return (Long) method.invoke(obj);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format("Method getId doesn't defined for data class %s", obj.getClass()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Illegal access to getId for data class %s", obj.getClass()), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    String.format("Illegal object to invoke getId for data class %s", obj.getClass()), e);
        }
    }
}
