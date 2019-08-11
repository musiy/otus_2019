package ru.otus.cache;

/**
 * Интерфейс для описания подписчика на события изменения кеша
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public interface Listener<K, V> {

    /**
     * Вызывается при добавлении ключа в кеш.
     *
     * @param key   ключ
     * @param value значение
     */
    void onPut(K key, V value);
}
