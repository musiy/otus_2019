package atm;

import par.Par;

import java.util.*;


/**
 * Реализация методов для всех банкоматов
 */
public class AtmImpl implements Atm {

    private Map<Par, Cartridge> parCartridges = new TreeMap<>((o1, o2) -> o2.getValue() - o1.getValue());

    AtmImpl(AtmConfig atmConfig) {
        for (var entry : atmConfig.getCellConfig().entrySet()) {
            parCartridges.put(entry.getKey(), new Cartridge(entry.getValue(), 0));
        }
    }

    @Override
    public void putMoney(Par par, int amount) {
        Cartridge cartridge = getCartridge(par);
        cartridge.add(amount);
    }

    @Override
    public Map<Par, Integer> giveMoney(int amount) {
        Map<Par, Integer> result = new HashMap<>();
        Iterator<Par> it = parCartridges.keySet().iterator();
        while (amount > 0 && it.hasNext()) {
            Par par = it.next();
            int value = par.getValue();
            int cnt = amount / value;
            result.put(par, cnt);
            amount -= cnt * value;
        }
        if (amount != 0) {
            throw new RuntimeException("нет размена");
        }
        for (var entry : result.entrySet()) {
            Cartridge cartridge = getCartridge(entry.getKey());
            cartridge.take(entry.getValue());
        }
        return result;
    }

    @Override
    public int getTotalAmount() {
        return parCartridges.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue().getCount())
                .sum();
    }

    private Cartridge getCartridge(Par par) {
        if (!parCartridges.containsKey(par)) {
            throw new IllegalArgumentException("ATM has no cartridge with par " + par);
        }
        return parCartridges.get(par);
    }
}
