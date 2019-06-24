package atm.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Организует стек состояний.
 * После добавления в стек, число элементов не может быть меньше одного.
 */
public class CaretakerImpl implements Caretaker {

    private Deque<Memento> stack = new ArrayDeque<>();

    @Override
    public void save(Memento memento) {
        stack.push(memento);
    }

    @Override
    public Memento getLast() {
        if (stack.size() == 0) {
            throw new NoSuchElementException();
        }
        if (stack.size() == 1) {
            return stack.getLast();
        }
        return stack.pop();
    }
}
