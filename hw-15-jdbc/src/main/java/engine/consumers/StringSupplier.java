package engine.consumers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class StringSupplier implements Function<String, Object> {

    private ResultSet rs;

    public StringSupplier(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public String apply(String alias) {
        try {
            return rs.getString(alias);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
