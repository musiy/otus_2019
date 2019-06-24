package engine.exceptions;

public class ReflectionAccessException extends RuntimeException {

    public ReflectionAccessException(Exception e) {
        super(e);
    }
}
