package cacheservice;

import cache.Cache;
import cache.CacheImpl;
import org.hibernate.Session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DbServiceImpl implements DbService {

    private final Cache<String, Object> cache;

    public DbServiceImpl() {
        // 60 секунд
        cache = new CacheImpl<>(1_000 * 60);
    }

    public void saveOrUpdate(Session session, Object obj) {
        session.saveOrUpdate(obj);
        String cacheKey = getCacheKey(obj.getClass(), getId(obj));
        cache.put(cacheKey, obj);
    }

    @Override
    public <T> T get(Session session, Class<T> clazz, long id) {
        String cacheKey = getCacheKey(clazz, id);
        Object o = cache.get(cacheKey);
        if (o != null) {
            if (o.getClass() != clazz) {
                throw new IllegalStateException("Сохраненный объект в кеше отличается по типу от ожидаемого");
            }
            return (T) o;
        }
        return session.get(clazz, id);
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
