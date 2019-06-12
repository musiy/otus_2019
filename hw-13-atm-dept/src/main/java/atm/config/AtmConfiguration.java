package atm.config;

import atm.cartridge.Cartridge;
import par.Par;

import java.util.Map;

public interface AtmConfiguration {

    Map<Par, Cartridge> getCartridgesByPar();
}
