package atm;

import par.Par;

import java.util.*;


/**
 * Реализация методов для всех банкоматов
 */
public abstract class AbstractCommonAtm implements Atm {

    private Map<Par, Cartridge> parCartridges = new HashMap<>();

    private List<Par> sortedByValue;

    /**
     * Конфигурирование кассет банкомата
     */
    void config(Map<Par, Cell> cellConfig) {
        for (Par par : cellConfig.keySet()) {
            parCartridges.put(par, new Cartridge(cellConfig.get(par), 0));
        }
    }

    /**
     * Инициализация банкомата
     */
    void init() {
        sortedByValue = new ArrayList<>(parCartridges.keySet());
        sortedByValue.sort(Comparator.comparingInt(Par::getValue));
        Collections.reverse(sortedByValue);
    }

    @Override
    public void putMoney(Par par, int amount) {
        Cartridge cartridge = getCartridge(par);
        cartridge.add(amount);
    }

    @Override
    public Map<Par, Integer> giveMoney(int amount) {
        Map<Par, Integer> result = new HashMap<>();
        Iterator<Par> it = sortedByValue.iterator();
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
            cartridge.remove(entry.getValue());
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
