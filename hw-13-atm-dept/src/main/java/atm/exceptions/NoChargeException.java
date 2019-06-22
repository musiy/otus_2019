package atm.exceptions;

public class NoChargeException extends Exception {

    public NoChargeException() {
        super("ATM has no charge");
    }
}
