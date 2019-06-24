package atm.notify;

import atm.Atm;

public class GetBalanceEvent implements AtmEvent {

    private int balance;
    private Atm atm;


    public GetBalanceEvent(Atm atm, int balance) {
        this.balance = balance;
        this.atm = atm;
    }

    @Override
    public String toString() {
        return "GetBalanceEvent{" +
                "balance=" + balance +
                ", atm=" + atm +
                '}';
    }
}
