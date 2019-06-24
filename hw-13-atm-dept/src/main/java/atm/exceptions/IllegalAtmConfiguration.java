package atm.exceptions;

import par.Par;

public class IllegalAtmConfiguration extends Exception {

    public IllegalAtmConfiguration(Par par) {
        super("Cartridge with par already exist: " + par);
    }


}
