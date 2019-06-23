package engine.exceptions;

import java.util.List;

public class AmbiguousSqlResult extends RuntimeException {

    public AmbiguousSqlResult(List list) {
        super("Ambiguous SQL result: too many or nothing: " + list.size());
    }
}
