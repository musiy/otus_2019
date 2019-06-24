package engine.creators;

import javax.json.Json;
import javax.json.JsonNumber;

class JsonValueCreatorLong implements JsonValueCreator<Long> {

    @Override
    public JsonNumber create(Long value) {
        return Json.createValue(value);
    }
}
