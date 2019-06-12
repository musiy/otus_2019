package atm.cartridge;

import java.io.Serializable;

/**
 * Бокс с банкнотами - ёмкость с купюрами.
 * Бокс не знает, какие номиналы купюр в нем находятся, но знает их количество.
 */
public class Cartridge implements Serializable {

    // число банкнот в ячейке
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

    public int take(int amount) {
        if (count - amount < 0) {
            throw new IllegalArgumentException("Can not obtain more bank notes then exists in cartridge");
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
