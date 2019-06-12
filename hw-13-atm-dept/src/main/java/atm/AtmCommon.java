package atm;

import atm.command.AtmCommand;
import atm.memento.Originator;
import atm.notify.AtmEvent;
import atm.notify.AtmEventSubscriber;

import java.util.ArrayList;
import java.util.List;

public abstract class AtmCommon implements Atm, Originator {

    private AtmCommon next;

    private List<AtmEventSubscriber> subscibers = new ArrayList<>();

    public AtmCommon addNext(AtmCommon next) {
        this.next = next;
        return next;
    }

    public AtmCommon getNext() {
        return next;
    }

    public void executeInChain(AtmCommand command) {
        execute(command);
        if (getNext() != null) {
            getNext().executeInChain(command);
        }
    }

    public void addSubscriber(AtmEventSubscriber atmEventSubscriber) {
        subscibers.add(atmEventSubscriber);
    }

    public void removeSubscriber(AtmEventSubscriber atmEventSubscriber) {
        subscibers.remove(atmEventSubscriber);
    }

    void notifyEvent(AtmEvent atmEvent) {
        subscibers.forEach(s -> s.update(atmEvent));
    }
}
