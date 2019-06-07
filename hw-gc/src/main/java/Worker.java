import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Worker {

    private static final int DELAY = 10;

    /**
     * Число объектов, которые создаются единоразово
     */
    private static final int OBJECTS_PER_ALLOCATION = 1_000;

    private List<Object> list = new ArrayList<>();

    public void run() throws InterruptedException {
        while (true) {
            createObjectsAddToList(OBJECTS_PER_ALLOCATION);
            Thread.sleep(DELAY);
        }
    }

    private void createObjectsAddToList(int count) {
        SampleObject s1 = null, s2 = null, s3 = null;
        for (int i = 0; i < count; i++) {
            SampleObject sampleObject = new SampleObject("Roman", "Musiy",
                    LocalDate.now(),
                    Long.decode("1234567890"));
            if (i % 2 == 0) {
                // каждый второй объект сохраняем
                list.add(sampleObject);
                continue;
            }
            // немного усложним задачу GC - добавим циклические ссылки
            if (s1 == null) {
                s1 = sampleObject;
            } else if (s2 == null) {
                s2 = sampleObject;
            } else if (s3 == null) {
                s3 = sampleObject;
            } else {
                s1.setLinkToNext(s2);
                s2.setLinkToNext(s3);
                s3.setLinkToNext(s1);
                s1 = null;
                s2 = null;
                s3 = null;
            }
        }
    }
}
