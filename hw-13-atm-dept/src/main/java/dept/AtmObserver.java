package dept;

import atm.notify.AtmEvent;
import atm.notify.AtmEventSubscriber;

public class AtmObserver implements AtmEventSubscriber {

    @Override
    public void update(AtmEvent atmEvent) {
        System.out.println("Obtained event: " + atmEvent);
    }
}
