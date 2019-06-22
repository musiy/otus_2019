package atm.command;

import atm.Atm;

/**
 * Собирает баланс со всех переданных банкоматов.
 */
public class GrabBalanceCommand implements AtmCommand {

    private int totalBalance = 0;

    @Override
    public void run(Atm atm) {
        totalBalance += atm.getBalance();
    }

    public int getTotalBalance() {
        return totalBalance;
    }
}
