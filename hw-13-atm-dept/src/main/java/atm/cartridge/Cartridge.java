package atm.cartridge;

import atm.exceptions.CartridgeOutOfMoneyException;

import java.io.Serializable;

/**
 * Бокс с банкнотами - ёмкость с купюрами.
 * Бокс не знает, какие номиналы купюр в нем находятся, но знает их количество.
 */
public class Cartridge implements Serializable {

    /**
     * Число банкнот в ячейке
     */
    private int count;

    public Cartridge(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int add(int amount) {
        count += amount;
        return count;
    }

    public int take(int amount) throws Exception {
        if (count - amount < 0) {
            throw new CartridgeOutOfMoneyException();
        }
        count -= amount;
        return count;
    }

    @Override
    public String toString() {
        return "atm.cartridge.Cartridge{" +
                "count=" + count +
                '}';
    }
}
