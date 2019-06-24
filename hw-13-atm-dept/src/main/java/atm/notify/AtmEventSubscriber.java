package atm.notify;

@FunctionalInterface
public interface AtmEventSubscriber {

    void update(AtmEvent atmEvent);
}
