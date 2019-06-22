package engine.creators;

import javax.json.Json;
import javax.json.JsonValue;

class JsonValueCreatorShort implements JsonValueCreator<Short> {

    @Override
    public JsonValue create(Short value) {
        return Json.createValue(value.intValue());
    }
}
