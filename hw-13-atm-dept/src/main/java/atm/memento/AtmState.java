package atm.memento;

import atm.cartridge.Cartridge;
import par.Par;

import java.util.HashMap;
import java.util.Map;

public class AtmState {

    private Map<Par, Cartridge> cartridges = new HashMap<>();

    public AtmState(Map<Par, Cartridge> cartridges) {
        cartridges.forEach((key, value) -> this.cartridges.put(key, new Cartridge(value.getCount())));
    }

    public Map<Par, Cartridge> getCartridges() {
        return cartridges;
    }
}
