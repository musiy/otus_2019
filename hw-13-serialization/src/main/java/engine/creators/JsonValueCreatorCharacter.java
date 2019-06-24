package engine.creators;

import javax.json.Json;
import javax.json.JsonValue;

class JsonValueCreatorCharacter implements JsonValueCreator<Character> {

    @Override
    public JsonValue create(Character value) {
        return Json.createValue(value.toString());
    }
}
