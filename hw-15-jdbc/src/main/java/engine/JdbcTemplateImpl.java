package engine;

import anno.Id;
import engine.consumers.ParamsConsumerSupplierFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {

    private ParamsConsumerSupplierFactory paramConsumerFactory = new ParamsConsumerSupplierFactory();

    private static final String INSERT_TEMPLATE = "INSERT INTO $table_name ($fields) VALUES ($wildcards)";

    private static final String UPDATE_TEMPLATE = "UPDATE $table_name SET $set_statement WHERE $id = ?";

    private static final String SELECT_TEMPLATE = "SELECT $fields FROM $table_name WHERE $id = ?";

    private Connection connection;

    private Class<T> clazz;

    private String insertQuery;

    private String updateQuery;

    private String selectQuery;

    private List<Field> fields = new ArrayList<>();

    private Field idField;

    public JdbcTemplateImpl(Connection connection, Class<T> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        verifyAnnotationExist(clazz);
        insertQuery = makeInsertQuery();
        updateQuery = makeUpdateQuery();
        selectQuery = makeSelectQuery();
    }

    private String makeInsertQuery() {
        List<String> fieldNames = fields.stream().map(Field::getName).collect(Collectors.toList());
        String fields = String.join(", ", fieldNames.toArray(new String[0]));
        StringBuilder wildcards = new StringBuilder();
        for (int i = 0; i < this.fields.size(); i++) {
            wildcards.append(i == 0 ? "?" : ", ?");
        }
        return INSERT_TEMPLATE
                .replace("$table_name", clazz.getSimpleName().toLowerCase())
                .replace("$fields", fields)
                .replace("$wildcards", wildcards.toString());
    }

    private String makeUpdateQuery() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.fields.size(); i++) {
            Field field = fields.get(i);
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(field.getName()).append(" = ?");
        }
        return UPDATE_TEMPLATE
                .replace("$table_name", clazz.getSimpleName().toLowerCase())
                .replace("$set_statement", sb.toString())
                .replace("$id", idField.getName());
    }

    private String makeSelectQuery() {
        List<String> fieldNames = fields.stream().map(Field::getName).collect(Collectors.toList());
        fieldNames.add(idField.getName());
        String fields = String.join(", ", fieldNames.toArray(new String[0]));
        return SELECT_TEMPLATE
                .replace("$table_name", clazz.getSimpleName().toLowerCase())
                .replace("$fields", fields)
                .replace("$id", idField.getName());
    }

    @Override
    public void create(T obj) {
        try {
            int id = executeInsert(insertQuery, obj);
            setFieldValue(idField, obj, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeInsert(String sql, T obj) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                BiConsumer<Integer, Object> consumer = paramConsumerFactory.paramConsumer(field.getType(), ps);
                consumer.accept(i + 1, getFieldValue(field, obj));
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new RuntimeException("Inserting failed");
                }
            }
        }
    }

    private Object getFieldValue(Field field, Object o) {
        boolean canAccess = field.canAccess(o);
        field.setAccessible(true);
        try {
            Object value = field.get(o);
            field.setAccessible(canAccess);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object setFieldValue(Field field, Object obj, Object value) {
        boolean canAccess = field.canAccess(obj);
        field.setAccessible(true);
        try {
            field.set(obj, value);
            field.setAccessible(canAccess);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T obj) {
        try {
            executeUpdate(obj);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeUpdate(T obj) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                BiConsumer<Integer, Object> consumer = paramConsumerFactory.paramConsumer(field.getType(), ps);
                consumer.accept(i + 1, getFieldValue(field, obj));
            }
            long id = (long) getFieldValue(idField, obj);
            ps.setLong(fields.size() + 1, id);
            return ps.executeUpdate();
        }
    }

    @Override
    public T load(long id) {
        try {
            List<T> list = executeQuery(selectQuery, id, this::objectFromResultSet);
            if (list.size() == 1) {
                return list.get(0);
            } else {
                throw new RuntimeException("Too many or empty result");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private T objectFromResultSet(ResultSet rs) {
        T object = createObject();
        for (Field field : fields) {
            Function<String, Object> supplier = paramConsumerFactory.paramSupplier(field.getType(), rs);
            Object value = supplier.apply(field.getName());
            setFieldValue(field, object, value);
        }
        Function<String, Object> supplier = paramConsumerFactory.paramSupplier(idField.getType(), rs);
        Object value = supplier.apply(idField.getName());
        setFieldValue(idField, object, value);
        return object;
    }

    private T createObject() {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        @SuppressWarnings("unchecked")
        Constructor<T> defaultConstructor = (Constructor<T>) constructors[0];
        try {
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private List<T> executeQuery(String sql, long id, Function<ResultSet, T> toObject) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                List<T> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(toObject.apply(resultSet));
                }
                return list;
            }
        }
    }

    private void verifyAnnotationExist(Class<?> clazz) {
        int count = 0;
        idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == Id.class) {
                    idField = field;
                    count++;
                }
            }
            if (field != idField) {
                fields.add(field);
            }
        }
        if (count == 0) {
            throw new RuntimeException("Data class should contain primary key marked with @Id annotation");
        } else if (count > 1) {
            throw new RuntimeException("Only one field should be marked with @Id annotation");
        }
    }

}
