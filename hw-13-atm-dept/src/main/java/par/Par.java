package par;

//  Номиналы
public enum Par {

    C10(10),
    C50(50),
    C100(100),
    C200(200),
    C500(500),
    C1000(1000),
    C2000(2000),
    C5000(5000);

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
