package engine.exceptions;

public class NoIdAnnotation extends RuntimeException {

    public NoIdAnnotation() {
        super("Data class should contain primary key marked with @Id annotation");
    }
}
