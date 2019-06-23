package engine.creators;

import javax.json.JsonNumber;

class JsonValueCreatorDouble implements JsonValueCreator<Double> {

    @Override
    public JsonNumber create(Double value) {

        return new FloatPointedClassWorkAround() {

            @Override
            public String toString() {
                return value.toString();
            }
        };
    }
}
