package par;

import java.util.HashMap;
import java.util.Map;

public class ParServiceImpl implements ParService {

    private Map<Integer, Par> storage = new HashMap<>();

    /**
     * Заполняем банкноты с их номиналами в приложении
     */
    public void init() {
        storage.put(100, createPar(100));
        storage.put(200, createPar(200));
        storage.put(500, createPar(500));
        storage.put(1000, createPar(1000));
        storage.put(2000, createPar(2000));
        storage.put(5000, createPar(5000));
    }

    private Par createPar(Integer value) {
        return new Par(value);
    }

    @Override
    public Par getPar(Integer value) {
        if (storage.containsKey(value)) {
            return storage.get(value);
        }
        throw new IllegalArgumentException("Par not found: " + value);
    }
}
