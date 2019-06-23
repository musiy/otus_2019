package engine.creators;

import javax.json.JsonValue;

public interface JsonValueCreator<T> {

    JsonValue create(T value);
}
