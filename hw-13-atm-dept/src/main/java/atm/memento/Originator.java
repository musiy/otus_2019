package atm.memento;

/**
 * Интерфейс для поддержки сохранения и восстановления состояния
 */
public interface Originator {

    Memento makeSnapshot();

    void restoreFromSnapshot(Memento memento);
}
