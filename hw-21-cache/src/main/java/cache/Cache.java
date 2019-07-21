package cache;

import java.util.function.Function;

/**
 * Интерфейс для работы с кешем
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public interface Cache<K, V> {

    /**
     * Добавить в кеш пару ключ/значение
     */
    void put(K key, V value);

    /**
     * Получить из кеша значение по ключу
     */
    V get(K key);

    /**
     * Версия метода {@link #get(K key)}, которая вычисляет значение, если его нет в кеше
     *
     * @param key
     * @param f
     * @return
     */
    V get(K key, Function<K, V> f);

    /**
     * Возвращает хиты
     */
    int getHitCount();

    /**
     * Возвращает миссы кеша
     */
    int getMissCount();

    /**
     * Освобождает кеш
     */
    void dispose();

    /**
     * Добавляет подписчика, который хочет слушать события об изменениях в кеше
     */
    void addListener(Listener<K, V> listener);

    /**
     * Удаляет подписчика - слушателя событий
     */
    void deleteListener(Listener<K, V> listener);
}
