package engine;

import engine.consumers.ParamsSupplierFactory;
import engine.exceptions.AmbiguousSqlResult;
import engine.exceptions.ReflectionAccessException;
import engine.functions.FunctionWithSqlException;
import engine.reflections.ClassMetaInfo;
import engine.reflections.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {

    private ParamsSupplierFactory paramConsumerFactory = new ParamsSupplierFactory();

    private Connection connection;

    private Class<T> clazz;

    /**
     * Запрос для вставки данных INSERT
     */
    private String insertQuery;

    /**
     * Запрос для обновления данных UPDATE
     */
    private String updateQuery;

    /**
     * Запрос выборки данных
     */
    private String selectQuery;

    /**
     * Мета информация о структуре класса
     */
    private ClassMetaInfo classMetaInfo;

    public JdbcTemplateImpl(Connection connection, Class<T> clazz) {
        this.connection = connection;
        this.clazz = clazz;
        classMetaInfo = ClassMetaInfo.fromClass(clazz);

        insertQuery = makeInsertQuery();
        updateQuery = makeUpdateQuery();
        selectQuery = makeSelectQuery();
    }

    private String makeInsertQuery() {
        String fields = String.join(", ", classMetaInfo.getFieldNames().toArray(new String[0]));
        StringBuilder wildcards = new StringBuilder();
        for (int i = 0; i < classMetaInfo.getFields().size(); i++) {
            wildcards.append(i == 0 ? "?" : ", ?");
        }
        final String insertQueryTemplate = "INSERT INTO $table_name ($fields) VALUES ($wildcards)";
        return insertQueryTemplate
                .replace("$table_name", clazz.getSimpleName().toLowerCase())
                .replace("$fields", fields)
                .replace("$wildcards", wildcards);
    }

    private String makeUpdateQuery() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classMetaInfo.getFields().size(); i++) {
            Field field = classMetaInfo.getFields().get(i);
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(field.getName()).append(" = ?");
        }
        final String updateQueryTemplate = "UPDATE $table_name SET $set_statement WHERE $id = ?";
        return updateQueryTemplate
                .replace("$table_name", clazz.getSimpleName().toLowerCase())
                .replace("$set_statement", sb.toString())
                .replace("$id", classMetaInfo.getIdField().getName());
    }

    private String makeSelectQuery() {
        List<String> fieldNames = classMetaInfo.getFields().stream().map(Field::getName).collect(Collectors.toList());
        fieldNames.add(classMetaInfo.getIdField().getName());
        String fields = String.join(", ", fieldNames.toArray(new String[0]));
        final String selectQueryTemplate = "SELECT $fields FROM $table_name WHERE $id = ?";
        return selectQueryTemplate
                .replace("$table_name", clazz.getSimpleName().toLowerCase())
                .replace("$fields", fields)
                .replace("$id", classMetaInfo.getIdField().getName());
    }

    @Override
    public void create(T obj) throws SQLException {
        int id = executeInsert(obj);
        setFieldValue(classMetaInfo.getIdField(), obj, id);
    }

    private int executeInsert(T obj) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < classMetaInfo.getFields().size(); i++) {
                Field field = classMetaInfo.getFields().get(i);
                ps.setObject(i + 1, getFieldValue(field, obj));
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public void update(T obj) throws SQLException {
        executeUpdate(obj);
    }

    private int executeUpdate(T obj) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            for (int i = 0; i < classMetaInfo.getFields().size(); i++) {
                Field field = classMetaInfo.getFields().get(i);
                ps.setObject(i + 1, getFieldValue(field, obj));
            }
            long id = (long) getFieldValue(classMetaInfo.getIdField(), obj);
            ps.setLong(classMetaInfo.getFields().size() + 1, id);
            return ps.executeUpdate();
        }
    }

    @Override
    public T load(long id) throws SQLException {
        List<T> list = executeQuery(selectQuery, id, this::objectFromResultSet);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new AmbiguousSqlResult(list);
        }
    }

    private T objectFromResultSet(ResultSet rs) {
        T object = ReflectionHelper.createObject(clazz);
            for (Field field : classMetaInfo.getFields()) {
            Function<String, Object> supplier = paramConsumerFactory.paramSupplier(field.getType(), rs);
            Object value = supplier.apply(field.getName());
            setFieldValue(field, object, value);
        }
        Function<String, Object> supplier = paramConsumerFactory.paramSupplier(classMetaInfo.getIdField().getType(), rs);
        Object value = supplier.apply(classMetaInfo.getIdField().getName());
        setFieldValue(classMetaInfo.getIdField(), object, value);
        return object;
    }

    private List<T> executeQuery(String sql, long id, FunctionWithSqlException<ResultSet, T> toObject) throws SQLException {
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

    private Object getFieldValue(Field field, Object obj) {
        try {
            return ReflectionHelper.getFieldValue(field, obj);
        } catch (IllegalAccessException e) {
            throw new ReflectionAccessException(e);
        }
    }

    private void setFieldValue(Field field, Object obj, Object value) {
        try {
            ReflectionHelper.setFieldValue(field, obj, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionAccessException(e);
        }
    }
}
