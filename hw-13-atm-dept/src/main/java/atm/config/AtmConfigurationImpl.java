package atm.config;

import atm.cartridge.Cartridge;
import par.Par;

import java.util.HashMap;
import java.util.Map;

public class AtmConfigurationImpl implements AtmConfiguration {

    private Map<Par, Cartridge> cartridgesByPar;

    private AtmConfigurationImpl() {
    }

    @Override
    public Map<Par, Cartridge> getCartridgesByPar() {
        return cartridgesByPar;
    }

    public static class Builder {

        private Map<Par, Cartridge> cartridgesByPar = new HashMap<>();

        public AtmConfiguration build() {
            var atmConfiguration = new AtmConfigurationImpl();
            atmConfiguration.cartridgesByPar = cartridgesByPar;
            return atmConfiguration;
        }

        public Builder addCartridge(Par par, int count) {
            if (cartridgesByPar.containsKey(par)) {
                throw new IllegalArgumentException("Cartridge with par already exist: " + par);
            }
            cartridgesByPar.put(par, new Cartridge(count));
            return this;
        }
    }
}
