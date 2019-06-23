package engine.creators;

import javax.json.Json;
import javax.json.JsonNumber;

class JsonValueCreatorInteger implements JsonValueCreator<Integer> {

    @Override
    public JsonNumber create(Integer value) {
        return Json.createValue(value);
    }
}
