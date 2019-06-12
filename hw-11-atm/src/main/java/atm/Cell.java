package atm;

/**
 * Ячейки для кассет банкомата
 */
public enum Cell {

    CellA,
    CellB,
    CellC,
    CellD,
    CellE,
    CellF;

    @Override
    public String toString() {
        return name();
    }
}
