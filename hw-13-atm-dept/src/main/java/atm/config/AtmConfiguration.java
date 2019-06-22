package atm.config;

import atm.cartridge.Cartridge;
import par.Par;

import java.util.Map;

/**
 * Конфигурация банкоматов.
 * Задаются номиналы и картриджы для банкнот.
 */
public interface AtmConfiguration {

    Map<Par, Cartridge> getCartridgesByPar();
}
