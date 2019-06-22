package atm.memento;

/**
 * Интерфейс для хранения и восстановления состояния
 */
public interface Caretaker {

    void save(Memento memento);

    Memento getLast();
}
