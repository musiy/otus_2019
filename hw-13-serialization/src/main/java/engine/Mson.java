package engine;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public final class Mson {

    public String toJson(Object object) {

        StringWriter stWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
            if (isIterable(object)) {
                JsonArray jab = arrayToJsonTree(object).build();
                jsonWriter.writeArray(jab);
            } else {
                JsonObject jo = objectToJsonTree(object).build();
                jsonWriter.writeObject(jo);
            }
        }
        return stWriter.toString();
    }

    private boolean isIterable(Object o) {
        return o.getClass().isArray() || o instanceof Collection;
    }

    private JsonArrayBuilder arrayToJsonTree(Object o) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        if (o.getClass().isArray()) {
            int length = Array.getLength(o);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(o, i);
                consume(jab, element);
            }
        } else {
            Collection c = (Collection) o;
            for (Object element : c) {
                consume(jab, element);
            }
        }
        return jab;
    }

    private void consume(JsonArrayBuilder jab, Object element) {
        if (isIterable(element)) {
            jab.add(arrayToJsonTree(element));
        } else {
            Class clazz = element.getClass();
            switch (clazz.getName()) {
                case "java.lang.String":
                    jab.add((String) element);
                    break;
                case "byte":
                case "java.lang.Byte":
                    jab.add((Byte) element);
                    break;
                case "char":
                case "java.lang.Character":
                    jab.add(String.valueOf(element));
                    break;
                case "int":
                case "java.lang.Integer":
                    jab.add((Integer) element);
                    break;
                case "short":
                case "java.lang.Short":
                    jab.add((Short) element);
                    break;
                case "long":
                case "java.lang.Long":
                    jab.add((Long) element);
                    break;
                case "float":
                case "java.lang.Float":
                    jab.add((Float) element);
                    break;
                case "double":
                case "java.lang.Double":
                    jab.add((Double) element);
                    break;
                default:
                    jab.add(objectToJsonTree(element));
            }
        }

    }

    private JsonObjectBuilder objectToJsonTree(Object o) {

        JsonObjectBuilder jxob = Json.createObjectBuilder();
        Field[] fields = o.getClass().getDeclaredFields();

        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                continue;
            }
            Object value = getFieldValue(field, o);
            if (value == null) {
                continue;
            }
            if (isIterable(value)) {
                jxob.add(field.getName(), arrayToJsonTree(value));
                continue;
            }
            Class clazz = field.getType();
            switch (clazz.getName()) {
                case "java.lang.String":
                    jxob.add(field.getName(), (String) value);
                    break;
                case "byte":
                case "java.lang.Byte":
                    jxob.add(field.getName(), (Byte) value);
                    break;
                case "char":
                case "java.lang.Character":
                    jxob.add(field.getName(), String.valueOf(value));
                    break;
                case "int":
                case "java.lang.Integer":
                    jxob.add(field.getName(), (Integer) value);
                    break;
                case "short":
                case "java.lang.Short":
                    jxob.add(field.getName(), (Short) value);
                    break;
                case "long":
                case "java.lang.Long":
                    jxob.add(field.getName(), (Long) value);
                    break;
                case "float":
                case "java.lang.Float":
                    jxob.add(field.getName(), (Float) value);
                    break;
                case "double":
                case "java.lang.Double":
                    jxob.add(field.getName(), (Double) value);
                    break;
                default:
                    jxob.add(field.getName(), objectToJsonTree(value));
            }
        }
        return jxob;
    }

    private Object getFieldValue(Field field, Object object) {
        boolean accessible = field.canAccess(object);
        field.setAccessible(true);
        try {
            Object value = field.get(object);
            field.setAccessible(accessible);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
