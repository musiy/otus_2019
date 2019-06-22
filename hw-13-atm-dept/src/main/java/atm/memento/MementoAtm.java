package atm.memento;

/**
 * Хранит состояние банкомата
 */
public class MementoAtm implements Memento<AtmState> {

    private AtmState atmState;

    @Override
    public void setState(AtmState atmState) {
        this.atmState = atmState;
    }

    @Override
    public AtmState getState() {
        return atmState;
    }
}
