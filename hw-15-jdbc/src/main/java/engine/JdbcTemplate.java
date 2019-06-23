package engine;

import java.sql.SQLException;

public interface JdbcTemplate<T> {

    void create(T objectData) throws SQLException;

    void update(T objectData) throws SQLException;

    T load(long id) throws SQLException;
}
