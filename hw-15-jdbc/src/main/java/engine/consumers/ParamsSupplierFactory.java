package engine.consumers;

import java.sql.ResultSet;
import java.util.function.Function;

public class ParamsSupplierFactory {

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
