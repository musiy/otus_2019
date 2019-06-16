package engine.consumers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class LongSupplier implements Function<String, Object> {

    private ResultSet rs;

    public LongSupplier(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public Long apply(String fieldName) {
        try {
            return rs.getLong(fieldName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
