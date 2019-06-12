package atm.memento;

public interface Memento<T> {

    void setState(T t);

    T getState();
}
