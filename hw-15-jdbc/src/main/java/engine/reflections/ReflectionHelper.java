package engine.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Optional;

public class ReflectionHelper {

    static Optional<Field> getFieldWithAnnotation(Class<?> dataClass,
                                                  Class<? extends Annotation> targetAnnotation) {
        Field annoField = null;
        for (Field field : dataClass.getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == targetAnnotation) {
                    if (annoField != null) {
                        throw new RuntimeException(
                                MessageFormat.format("Only one field with annotation {0} expected {1}",
                                        targetAnnotation.getName(), field.getName()));
                    }
                    annoField = field;
                }
            }
        }
        return Optional.ofNullable(annoField);
    }

    public static <T> T createObject(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        @SuppressWarnings("unchecked")
        Constructor<T> defaultConstructor = (Constructor<T>) constructors[0];
        try {
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Field field, Object o) throws IllegalAccessException {
        boolean canAccess = field.canAccess(o);
        field.setAccessible(true);
        Object value = field.get(o);
        field.setAccessible(canAccess);
        return value;
    }

    public static void setFieldValue(Field field, Object obj, Object value) throws IllegalAccessException {
        boolean canAccess = field.canAccess(obj);
        field.setAccessible(true);
        field.set(obj, value);
        field.setAccessible(canAccess);
    }
}
