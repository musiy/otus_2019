package atm.exceptions;

public class CartridgeOutOfMoneyException extends Exception {

    public CartridgeOutOfMoneyException() {
        super("Can not obtain more bank notes then exists in cartridge");
    }
}
