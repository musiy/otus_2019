package engine.creators;

import javax.json.Json;
import javax.json.JsonValue;

class JsonValueCreatorByte implements JsonValueCreator<Byte> {

    @Override
    public JsonValue create(Byte value) {
        return Json.createValue(value.intValue());
    }
}
