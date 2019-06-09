import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class ObjectStorage {

    private List<Object> list = new ArrayList<>();

    private AtomicInteger created = new AtomicInteger();

    private Random random = new Random();

    ObjectStorage() {
        random.setSeed(System.currentTimeMillis());
    }

    void createObjectsAddToList(int count) {
//        SampleObject s1 = null;
//        SampleObject s2 = null;
//        SampleObject s3 = null;
        for (int i = 0; i < count; i++) {
            SampleObject sampleObject = new SampleObject("Roman", "Musiy",
                    LocalDate.now(),
                    Long.decode("1234567890"));
            list.add(sampleObject);
            created.incrementAndGet();
//            // немного усложним задачу GC - добавим циклические ссылки
//            if (s1 == null) {
//                s1 = sampleObject;
//            } else if (s2 == null) {
//                s2 = sampleObject;
//            } else if (s3 == null) {
//                s3 = sampleObject;
//            } else {
//                s1.setLinkToNext(s2);
//                s2.setLinkToNext(s3);
//                s3.setLinkToNext(s1);
//                s1 = null;
//                s2 = null;
//                s3 = null;
//            }
        }
    }

    void removeObjectsRandomly(int countToRemove) {
        for (int i = 0; i < countToRemove; i++) {
            int idx = random.nextInt(list.size());
            // не свмая быстрая операция
            list.remove(idx);
        }
    }

    List<Object> getList() {
        return list;
    }

    int getCreated() {
        return created.get();
    }

    void resetCounter() {
        created = new AtomicInteger();
    }
}
