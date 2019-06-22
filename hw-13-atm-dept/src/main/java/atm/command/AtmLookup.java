package atm.command;

import atm.Atm;

/**
 * Команда проверяет, что переданый банкомат - искомый.
 */
public class AtmLookup implements AtmCommand {

    private String id;

    private Atm atm;

    public AtmLookup(String id) {
        this.id = id;
    }

    @Override
    public void run(Atm atm) {
        if (atm.getId().equals(id)) {
            this.atm = atm;
        }
    }

    public Atm getAtm() {
        return atm;
    }
}
