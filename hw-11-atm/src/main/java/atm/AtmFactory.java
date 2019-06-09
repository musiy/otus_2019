package atm;

import par.Par;
import par.ParService;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика для создания банкоматов
 */
public class AtmFactory {

    private ParService parService;

    public AtmFactory(ParService parService) {
        this.parService = parService;
    }

    public AtmImpl createAtm() {
        return new AtmImpl(AtmConfig.fromConfig(getCellConfig()));
    }

    /**
     * Конфигурация ячеек и номиналов хранится где то во внешнем миире
     */
    private Map<Par, Cell> getCellConfig() {
        Map<Par, Cell> cellConfig = new HashMap<>();
        cellConfig.put(parService.getPar(100), Cell.CellA);
        cellConfig.put(parService.getPar(200), Cell.CellB);
        cellConfig.put(parService.getPar(500), Cell.CellC);
        cellConfig.put(parService.getPar(1000), Cell.CellD);
        cellConfig.put(parService.getPar(2000), Cell.CellE);
        cellConfig.put(parService.getPar(5000), Cell.CellF);
        return cellConfig;
    }
}
