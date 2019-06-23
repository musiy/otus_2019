package engine.creators;

import java.util.Arrays;

public class JsonValueCreatorFactory {

    public static JsonValueCreator fromType(Class<?> clazz) {
        TYPE_CATEGORY typeCategory = TYPE_CATEGORY.fromClass(clazz);
        switch (typeCategory) {
            case BYTE:
                return new JsonValueCreatorByte();
            case SHORT:
                return new JsonValueCreatorShort();
            case INTEGER:
                return new JsonValueCreatorInteger();
            case LONG:
                return new JsonValueCreatorLong();
            case FLOAT:
                return new JsonValueCreatorFloat();
            case DOUBLE:
                return new JsonValueCreatorDouble();
            case CHARACTER:
                return new JsonValueCreatorCharacter();
            case STRING:
                return new JsonValueCreatorString();
            default:
                return null;
        }
    }


    private enum TYPE_CATEGORY {

        BYTE(byte.class, Byte.class),
        SHORT(short.class, Short.class),
        INTEGER(int.class, Integer.class),
        FLOAT(float.class, Float.class),
        LONG(long.class, Long.class),
        DOUBLE(double.class, Double.class),
        CHARACTER(char.class, Character.class),
        STRING(String.class),
        UNKNOWN;

        private Class<?>[] types;

        TYPE_CATEGORY(Class<?>... types) {
            this.types = types;
        }

        static TYPE_CATEGORY fromClass(Class<?> clazz) {
            return Arrays.stream(values())
                    .filter(creatorType -> Arrays.asList(creatorType.types).contains(clazz))
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }
}
