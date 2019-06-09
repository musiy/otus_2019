package par;

/**
 * номинал валюты
 */
public final class Par {

    private int value;

    Par(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Par{" +
                "value=" + value +
                '}';
    }
}
