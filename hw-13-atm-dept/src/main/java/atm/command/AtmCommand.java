package atm.command;

import atm.Atm;

/**
 * Общий интерфейс для всех комманд.
 * Команда выполняет опрацию над банкоматом - atm.
 */
public interface AtmCommand {

    void run(Atm atm);
}
