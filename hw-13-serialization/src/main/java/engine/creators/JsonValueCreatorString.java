package engine.creators;

import javax.json.Json;
import javax.json.JsonValue;

class JsonValueCreatorString implements JsonValueCreator<String> {

    @Override
    public JsonValue create(String value) {
        return Json.createValue(value);
    }
}
