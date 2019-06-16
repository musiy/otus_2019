package engine;

public interface JdbcTemplate<T> {

    void create(T objectData);

    void update(T objectData);

    T load(long id);
}
