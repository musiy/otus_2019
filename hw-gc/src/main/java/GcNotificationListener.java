import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class GcNotificationListener implements NotificationListener {

    private int totalGcTime = 0;

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
            GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

            String gcName = info.getGcName();
            String gcAction = info.getGcAction();
            String gcCause = info.getGcCause();

            long startTime = info.getGcInfo().getStartTime();
            long duration = info.getGcInfo().getDuration();

            totalGcTime += duration;

            System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction
                    + ", gcCause:" + gcCause + "(" + duration + " ms), total gc time: " + totalGcTime);
        }
    }

    public int getTotalGcTime() {
        return totalGcTime;
    }
}
