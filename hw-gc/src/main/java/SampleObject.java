import java.time.LocalDate;

public class SampleObject {

    private String firstName;
    private String secondName;
    private LocalDate birthDate;
    private Long inn;
    private int[] arr;

    private SampleObject linkToNext;

    public SampleObject(String firstName, String secondName, LocalDate birthDate, Long inn) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.inn = inn;
        this.arr = new int[16];
    }

    public void setLinkToNext(SampleObject linkToNext) {
        this.linkToNext = linkToNext;
    }
}
