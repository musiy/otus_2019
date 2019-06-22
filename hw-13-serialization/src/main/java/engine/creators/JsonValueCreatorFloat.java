package engine.creators;

import javax.json.JsonValue;

class JsonValueCreatorFloat implements JsonValueCreator<Float> {

    @Override
    public JsonValue create(Float value) {

        return new FloatPointedClassWorkAround() {
            
            @Override
            public String toString() {
                return value.toString();
            }
        };
    }
}
