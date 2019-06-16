package engine.consumers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ParamsConsumerSupplierFactory {

    public BiConsumer<Integer, Object> paramConsumer(Class<?> clazz, PreparedStatement ps) {
        if (clazz == Integer.class || clazz == int.class) {
            return new IntegerConsumer(ps);
        } else if (clazz == Long.class || clazz == long.class) {
            return new LongConsumer(ps);
        } else if (clazz == String.class) {
            return new StringConsumer(ps);
        }
        throw new IllegalArgumentException("Value consumer not defined for class: " + clazz);
    }

    public Function<String, Object> paramSupplier(Class<?> clazz, ResultSet rs) {
        if (clazz == Integer.class || clazz == int.class) {
            return new IntegerSupplier(rs);
        } else if (clazz == Long.class || clazz == long.class) {
            return new LongSupplier(rs);
        } else if (clazz == String.class) {
            return new StringSupplier(rs);
        }
        throw new IllegalArgumentException("Value supplier not defined for class: " + clazz);
    }
}
