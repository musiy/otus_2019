package atm;

import par.Par;

import java.util.Map;

class AtmConfig {

    private Map<Par, Cell> cellConfig;

    public Map<Par, Cell> getCellConfig() {
        return cellConfig;
    }

    static AtmConfig fromConfig(Map<Par, Cell> cellConfig) {
        AtmConfig atmConfig = new AtmConfig();
        atmConfig.cellConfig = cellConfig;
        return atmConfig;
    }
}
