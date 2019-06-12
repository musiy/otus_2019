package atm.memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class CaretakerImpl implements Caretaker {

    private Deque<Memento> stack = new ArrayDeque<>();

    @Override
    public void save(Memento memento) {
        stack.push(memento);
    }

    @Override
    public Memento getLast() {
        if (stack.size() == 0) {
            throw new IllegalStateException("No more elements");
        }
        if (stack.size() == 1) {
            return stack.getLast();
        }
        return stack.pop();
    }
}
