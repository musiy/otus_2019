package atm.memento;

public interface Originator {

    Memento makeSnapshot();

    void restoreFromSnapshot(Memento memento);
}
