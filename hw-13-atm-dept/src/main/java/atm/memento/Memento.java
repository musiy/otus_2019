package atm.memento;

/**
 * Мементо интерфейс для сохранения состояния
 * @param <T>
 */
public interface Memento<T> {

    void setState(T t);

    T getState();
}
