package par;

/**
 * номинал валюты
 */
public final class Par {

    private Integer value;

    Par(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Par{" +
                "value=" + value +
                '}';
    }
}
