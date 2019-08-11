package ru.otus.cache;

import org.springframework.stereotype.Service;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Имплементация кеша.
 */
@Service
public class CacheImpl<K, V> implements Cache<K, V> {

    /**
     * Собственно хранилище кеша
     */
    private final Map<K, Element<V>> storage = new ConcurrentHashMap<>();

    /**
     * Счетчик хитов
     */
    private int hitCount;

    /**
     * Счетчик промахов
     */
    private int missCount;

    /**
     * Список подписчиков
     */
    private final Collection<WeakReference<Listener<K, V>>> listeners = new ConcurrentLinkedQueue<>();

    /**
     * Период удаления устаревших значений в кеше и очистки листеннера.
     * Фактически разрешающая способность кеша.
     */
    private static final int DEFAULT_CLEAN_CACHE_PERIOD = 100;

    private static final int DEFAULT_INACCESSIBLE_TIME = 1_000 * 60;

    private Timer timer = new Timer();

    public CacheImpl() {
        int inaccessibleTime = DEFAULT_INACCESSIBLE_TIME;
        if (inaccessibleTime < DEFAULT_CLEAN_CACHE_PERIOD) {
            throw new IllegalArgumentException(
                    String.format("Inaccessible time parameter should be greater then %d ms",
                            DEFAULT_CLEAN_CACHE_PERIOD));
        }
        timer.schedule(new RemoveObsoleteStorageKeys(), DEFAULT_CLEAN_CACHE_PERIOD, DEFAULT_CLEAN_CACHE_PERIOD);
        timer.schedule(new RemoveObsoleteListeners(), DEFAULT_CLEAN_CACHE_PERIOD, DEFAULT_CLEAN_CACHE_PERIOD);
        timer.schedule(new RemoveItemsByAccessTime(inaccessibleTime), DEFAULT_CLEAN_CACHE_PERIOD, DEFAULT_CLEAN_CACHE_PERIOD);
    }

    @Override
    public void put(K key, V value) {
        if (value == null) {
            return;
        }
        storage.put(key, new Element<>(value));
        // синхронизация лисенеров
        listeners.forEach(ref -> {
            Listener<K, V> listener = ref.get();
            if (listener != null) {
                listener.onPut(key, value);
            }
        });
    }

    @Override
    public V get(K key) {
        Element<V> element = storage.get(key);
        if (element == null) {
            missCount++;
            return null;
        }
        V val = element.getRef();
        if (val == null) {
            // удяляем, если отсуствует
            storage.remove(key);
            // если элемент собран GC, то считаем это как мисс, т.к. фактически прийдётся вычислять его снова
            missCount++;
        } else {
            hitCount++;
        }
        return val;
    }

    @Override
    public V get(K key, Function<K, V> f) {
        V val = get(key);
        return val == null ? f.apply(key) : val;
    }

    @Override
    public int getHitCount() {
        return hitCount;
    }

    @Override
    public int getMissCount() {
        return missCount;
    }

    @Override
    public void dispose() {
        storage.clear();
    }

    /**
     * Установить обработчик для нотификации об изменениях в кеше.
     * Важная особенность - следует сохранять ссылку на обработчик,
     * иначе он может быть удален при первой сборке мусора.
     */
    @Override
    public void addListener(Listener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    /**
     * Удалить обработчик
     */
    @Override
    public void deleteListener(Listener<K, V> listener) {
        listeners.removeIf(ref -> ref.get() == listener);
    }

    /**
     * default область видимости для тестов
     *
     * @return
     */
    int size() {
        return storage.size();
    }

    /**
     * Бокс для хранения значения и метаинформации
     *
     * @param <V> тип значения
     */
    static class Element<V> {

        private final WeakReference<V> ref;

        private long creationTime;

        private long lastAccessTime;


        Element(V val) {
            Objects.requireNonNull(val);
            this.ref = new WeakReference<>(val);
            this.creationTime = getCurrentTime();
            this.lastAccessTime = getCurrentTime();
        }

        long getCurrentTime() {
            return System.currentTimeMillis();
        }

        V getRef() {
            lastAccessTime = getCurrentTime();
            return ref.get();
        }

        long getCreationTime() {
            return creationTime;
        }

        long getLastAccessTime() {
            return lastAccessTime;
        }
    }

    /**
     * Удяляет weak reference, если она больше не ссылается на объект
     */
    class RemoveObsoleteStorageKeys extends TimerTask {

        @Override
        public void run() {
            Set<K> toDelete = storage.entrySet().stream()
                    .filter(entry -> entry.getValue().ref.get() == null)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            toDelete.forEach(storage::remove);
        }
    }

    /**
     * Удяляет weak reference, если она больше не ссылается на объект
     */
    class RemoveObsoleteListeners extends TimerTask {

        @Override
        public void run() {
            listeners.removeIf(ref -> ref.get() == null);
        }
    }


    class RemoveItemsByAccessTime extends TimerTask {

        /**
         * Период времени после последнего доступа, в течении которого объект будет жить
         */
        private int maxInaccessibleTime;

        RemoveItemsByAccessTime(int maxInaccessibleTime) {
            this.maxInaccessibleTime = maxInaccessibleTime;
        }

        @Override
        public void run() {
            List<K> entriesToDelete = storage.entrySet().stream()
                    .filter(e -> e.getValue().ref.get() != null
                                 && e.getValue().lastAccessTime + maxInaccessibleTime < getCurrentTime())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (!entriesToDelete.isEmpty()) {
                entriesToDelete.forEach(storage::remove);
            }
        }

        long getCurrentTime() {
            return System.currentTimeMillis();
        }
    }

}
