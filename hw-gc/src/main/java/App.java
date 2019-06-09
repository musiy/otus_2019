import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

/**
 * Сравнение разных сборщиков мусора
 * Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа
 * (young, old) и время которое ушло на сборки в минуту.
 * <p>
 * Добиться OutOfMemory в этом приложении через медленное подтекание по памяти
 * (например добавлять элементы в List и удалять только половину).
 * <p>
 * Настроить приложение (можно добавлять Thread.sleep(...)) так чтобы оно падало
 * с OOM примерно через 5 минут после начала работы.
 * <p>
 * Собрать статистику (количество сборок, время на сборки) по разным GC.
 * <p>
 * !!! Сделать выводы !!!
 * ЭТО САМАЯ ВАЖНАЯ ЧАСТЬ РАБОТЫ:
 * Какой gc лучше и почему?
 * <p>

  -Xms512m
  -Xmx512m
  -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,level:filecount=5,filesize=10m
  -XX:+HeapDumpOnOutOfMemoryError
  -XX:HeapDumpPath=./logs/dump.bin
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=30
 */
public class App {

    public static void main(String[] args) throws Exception {
        ObjectStorage storage = new ObjectStorage();
        GcNotificationListener gcListener = new GcNotificationListener(storage);
        switchOnMonitoring(gcListener);
        long l = System.currentTimeMillis();
        try {
            new Worker(storage).run();
            System.out.println("Never should write this message");
        } finally {
            long duration = System.currentTimeMillis() - l;
            System.out.println("=== END OF APPLICATION LIFE == T_app = " + duration
                    + ", T_gc = " + gcListener.getTotalGcTime());
        }
    }

    private static void switchOnMonitoring(NotificationListener gcListener) {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(gcListener, null, null);
        }
    }
}
