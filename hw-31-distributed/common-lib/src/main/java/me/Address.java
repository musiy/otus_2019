package me;

/**
 * Адресат сообщения
 */
public class Address {

    /** Идентификатор компонента, что бы отличить один компонент от другого того же типа */
    private final String id;

    /** Тип компонента */
    private final Type type;

    public Address(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Address {%s:%s}", type.toString(), id);
    }

    public enum Type {
        FE,
        DB;
    }
}
