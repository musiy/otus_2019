package atm.memento;

public interface Caretaker {

    void save(Memento memento);

    Memento getLast();
}
