package cache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheTest {

    /**
     * Проверить функцию очистки объектов при отсутствии доступа
     */
    @Test
    void testInaccessibleTime() throws InterruptedException {
        int inaccessibleTime = 100;
        CacheImpl<Integer, SampleTestObject> cache = new CacheImpl<>(inaccessibleTime);
        Collection<SampleTestObject> sampleTestObjectCollection = new ArrayList<>();
        synchronized (this) {
            for (int i = 0; i < 255; i++) {
                SampleTestObject sampleTestObject = new SampleTestObject();
                sampleTestObjectCollection.add(sampleTestObject);
                cache.put(i, sampleTestObject);
            }
            assertEquals(cache.size(), sampleTestObjectCollection.size());
        }
        Thread.sleep(300);
        assertEquals(cache.size(), 0);
    }

    /**
     * Проверить, что обработчики удаляются, если на них нет ссылок
     */
    @Test
    void testObsoleteListenersAutoRemove() throws InterruptedException {
        int inaccessibleTime = 100;
        CacheImpl<Integer, SampleTestObject> cache = new CacheImpl<>(inaccessibleTime);
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicLong atomicLong = new AtomicLong();
        Listener<Integer, SampleTestObject> handler = (key, value) -> atomicLong.incrementAndGet();
        synchronized (this) {
            cache.addListener((key, value) -> atomicInteger.incrementAndGet());
            cache.addListener(handler);
            cache.put(1, new SampleTestObject());
            cache.put(2, new SampleTestObject());
            assertEquals(atomicInteger.get(), 2);
            assertEquals(atomicLong.get(), 2);
        }
        System.gc();
        Thread.sleep(300);
        cache.put(3, new SampleTestObject());
        assertEquals(atomicInteger.get(), 2);
        assertEquals(atomicLong.get(), 3);
    }

    /**
     * Проверка, что после сборки мусора выполнилась очистка WeakReference,
     * а вслед за ней удаление "пустышек" WeakReference.
     *
     * @throws InterruptedException
     */
    @Test
    void testGcRemoveWeakReferences() throws InterruptedException {
        int inaccessibleTime = 1_000 * 60;
        CacheImpl<Integer, SampleTestObject> cache = new CacheImpl<>(inaccessibleTime);
        synchronized (this) {
            for (int i = 0; i < 255; i++) {
                cache.put(i, new SampleTestObject());
            }
            assertEquals(cache.size(), 255);
        }
        System.gc();
        // подождём пока отработают внутренние сборщики пустых WeakReference
        Thread.sleep(300);
        assertEquals(cache.size(), 0);
    }

    static class SampleTestObject {
    }
}
