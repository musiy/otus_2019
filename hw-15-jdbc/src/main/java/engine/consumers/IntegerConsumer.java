package engine.consumers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class IntegerConsumer implements BiConsumer<Integer, Object> {

    private PreparedStatement ps;

    public IntegerConsumer(PreparedStatement ps) {
        this.ps = ps;
    }

    @Override
    public void accept(Integer idx, Object value) {
        try {
            ps.setInt(idx, (Integer) value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
