package atm.config;

import atm.cartridge.Cartridge;
import atm.exceptions.IllegalAtmConfiguration;
import par.Par;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация конфигурации банкомата
 */
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

        public Builder addCartridge(Par par, int count) throws Exception {
            if (cartridgesByPar.containsKey(par)) {
                throw new IllegalAtmConfiguration(par);
            }
            cartridgesByPar.put(par, new Cartridge(count));
            return this;
        }
    }
}
