package atm;

/**
 * Бокс с банкнотами.
 */
class Cartridge {

    // ячейка
    private Cell cell;

    // число банкнот в ячейке
    private int count;

    public Cartridge(Cell cell, int count) {
        this.cell = cell;
        this.count = count;
    }

    public Cell getCell() {
        return cell;
    }

    public int getCount() {
        return count;
    }

    public int add(int amount) {
        count += amount;
        return count;
    }

    public int remove(int amount) {
        if (count - amount < 0) {
            throw new IllegalArgumentException("Can not obtain more bank notes then exists in cartridge");
        }
        count -= amount;
        return count;
    }

    @Override
    public String toString() {
        return "Cartridge{" +
                "cell=" + cell +
                ", count=" + count +
                '}';
    }
}
