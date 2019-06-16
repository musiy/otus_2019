package engine.consumers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class IntegerSupplier implements Function<String, Object> {

    private ResultSet rs;

    public IntegerSupplier(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public Integer apply(String alias) {
        try {
            return rs.getInt(alias);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
