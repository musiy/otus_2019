package atm;

import atm.config.AtmConfiguration;
import atm.config.AtmConfigurationImpl;
import par.Par;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Фабрика для быстрого создания банкоматов с однотипной конфигурацией.
 */
public class AtmFactory {

    private AtomicInteger atomicInteger = new AtomicInteger();

    private Random random = new Random();

    public AtmCommon createAtm() throws Exception {
        String id = "ATM-" + atomicInteger.incrementAndGet();
        return new AtmImpl(getAtmConfiguration(), id);
    }

    private AtmConfiguration getAtmConfiguration() throws Exception {
        int count = random.nextInt(50) + 50;
        return new AtmConfigurationImpl.Builder()
                .addCartridge(Par.C50, count)
                .addCartridge(Par.C100, count)
                .addCartridge(Par.C500, count)
                .addCartridge(Par.C1000, count)
                .addCartridge(Par.C2000, count)
                .addCartridge(Par.C5000, count)
                .build();
    }
}
