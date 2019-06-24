package atm;

import atm.command.AtmCommand;
import par.Par;

import java.util.Map;

public interface Atm {

    String getId();

    int getBalance();

    Map<Par, Integer> issueMoney(int money) throws Exception;

    boolean checkHealth();

    void execute(AtmCommand command);
}
