package engine.consumers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class StringConsumer implements BiConsumer<Integer, Object> {

    private PreparedStatement ps;

    public StringConsumer(PreparedStatement ps) {
        this.ps = ps;
    }

    @Override
    public void accept(Integer idx, Object value) {
        try {
            ps.setString(idx, (String) value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
