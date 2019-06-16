package engine.consumers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class LongConsumer implements BiConsumer<Integer, Object> {

    private PreparedStatement ps;

    public LongConsumer(PreparedStatement ps) {
        this.ps = ps;
    }

    @Override
    public void accept(Integer idx, Object value) {
        try {
            ps.setLong(idx, (Long) value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
