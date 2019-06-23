package engine;

import engine.creators.JsonValueCreator;
import engine.creators.JsonValueCreatorFactory;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Mson {

    public String toJson(Object obj) {

        StringWriter stringWriter = new StringWriter();
        if (obj == null) {
            stringWriter.append("null");
            return stringWriter.toString();
        }
        try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
            jsonWriter.write(objectToJsonValue(obj));
        }
        return stringWriter.toString();
    }

    /**
     * JsonValue
     * JsonValue -> JsonNumber
     * JsonValue -> JsonString
     * JsonValue -> JsonStructure -> JsonObject
     * JsonValue -> JsonStructure -> JsonArray
     */
    @SuppressWarnings("unchecked")
    private JsonValue objectToJsonValue(Object obj) {

        JsonValueCreator jsonValueCreator = JsonValueCreatorFactory.fromType(obj.getClass());
        if (jsonValueCreator != null) {
            return jsonValueCreator.create(obj);
        }
        return isSequential(obj)
                ? fromSequence(obj)
                : fromObject(obj);
    }


    private boolean isSequential(Object o) {
        return o.getClass().isArray() || o instanceof Iterable;
    }

    private JsonArray fromSequence(Object obj) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                jab.add(objectToJsonValue(element));
            }
        } else {
            for (Object o : ((Iterable) obj)) {
                jab.add(objectToJsonValue(o));
            }
        }
        return jab.build();
    }

    private JsonStructure fromObject(Object obj) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                continue;
            }
            Object value = getFieldValue(field, obj);
            if (value == null) {
                continue;
            }
            jsonObjectBuilder.add(field.getName(), objectToJsonValue(value));
        }
        return jsonObjectBuilder.build();
    }

    private Object getFieldValue(Field field, Object obj) {
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            Object value = field.get(obj);
            field.setAccessible(accessible);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
