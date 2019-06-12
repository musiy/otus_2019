package dept;

import atm.AtmCommon;
import atm.command.CheckHealthCommand;
import atm.command.GrabBalanceCommand;
import atm.memento.Caretaker;
import atm.memento.CaretakerImpl;
import atm.memento.Memento;
import atm.memento.MementoAtm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class AtmDept {

    private AtmCommon head;

    private AtmObserver observer = new AtmObserver();

    private Map<AtmCommon, Caretaker> caretakers = new HashMap<>();

    private AtmDept() {
    }

    public int getBalance() {
        GrabBalanceCommand command = new GrabBalanceCommand();
        head.executeInChain(command);
        return command.getTotalBalance();
    }

    public boolean checkHealth() {
        CheckHealthCommand checkHealthCommand = new CheckHealthCommand();
        head.executeInChain(checkHealthCommand);
        return checkHealthCommand.getOutOfService().isEmpty();
    }

    public Optional<AtmCommon> findAtm(String id) {
        AtmCommon current = head;
        while (current != null) {
            if (current.getId().equals(id)) {
                return Optional.of(current);
            }
            current = current.getNext();
        }
        return Optional.empty();
    }

    private void saveAtmState() {
        AtmCommon current = head;
        while (current != null) {
            Caretaker caretaker = new CaretakerImpl();
            caretakers.put(current, caretaker);
            caretaker.save(current.makeSnapshot());
            current = current.getNext();
        }
    }

    public void restoreAtmState(AtmCommon atm) {
        if (!caretakers.containsKey(atm)) {
            throw new IllegalArgumentException();
        }
        Memento memento = caretakers.get(atm).getLast();
        atm.restoreFromSnapshot(memento);
    }

    public static class Builder {

        private AtmDept atmDept;

        public static Builder create() {
            Builder builder = new Builder();
            builder.atmDept = new AtmDept();
            return builder;
        }

        public AtmDept build() {
            atmDept.saveAtmState();
            return atmDept;
        }

        Builder addAtm(AtmCommon atmCommon) {
            atmCommon.addSubscriber(atmDept.observer);
            if (atmDept.head == null) {
                atmDept.head = atmCommon;
            } else {
                AtmCommon current = atmDept.head;
                while (current.getNext() != null) {
                    current = current.getNext();
                }
                current.addNext(atmCommon);
            }
            return this;
        }
    }
}
