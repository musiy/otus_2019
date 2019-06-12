package atm;

import par.Par;

import java.util.Map;

/**
 * Интерфейс банкомата
 */
public interface Atm {

    void putMoney(Par par, int amount);

    Map<Par, Integer> giveMoney(int amount);

    int getTotalAmount();
}
