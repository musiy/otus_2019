package engine.reflections;

import anno.Id;
import engine.exceptions.NoIdAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassMetaInfo {

    private Field idField;

    private List<Field> fields = new ArrayList<>();

    public static ClassMetaInfo fromClass(Class<?> clazz) {
        ClassMetaInfo meta = new ClassMetaInfo();
        meta.idField = ReflectionHelper.getFieldWithAnnotation(clazz, Id.class)
                .orElseThrow(NoIdAnnotation::new);
        meta.fields = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toList());
        meta.fields.remove(meta.idField);
        return meta;
    }

    public Field getIdField() {
        return idField;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<String> getFieldNames() {
        return fields.stream()
                .map(Field::getName)
                .collect(Collectors.toList());
    }
}
