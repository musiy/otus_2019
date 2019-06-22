package atm;

import atm.cartridge.Cartridge;
import atm.command.AtmCommand;
import atm.config.AtmConfiguration;
import atm.exceptions.NoChargeException;
import atm.memento.AtmState;
import atm.memento.Memento;
import atm.memento.MementoAtm;
import atm.notify.GetBalanceEvent;
import atm.notify.HealthCheckAtmEvent;
import atm.notify.IssueMoneyAtmEvent;
import par.Par;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class AtmImpl extends AtmCommon {

    private Map<Par, Cartridge> cartridgesByPar = new TreeMap<>((o1, o2) -> o2.getValue() - o1.getValue());

    private String id;

    AtmImpl(AtmConfiguration configuration, String id) {
        cartridgesByPar.putAll(configuration.getCartridgesByPar());
        this.id = id;
    }

    @Override
    public void execute(AtmCommand command) {
        command.run(this);
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getBalance() {
        int sum = cartridgesByPar.entrySet()
                .stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue().getCount())
                .sum();
        notifyEvent(new GetBalanceEvent(this, sum));
        return sum;
    }

    @Override
    public Map<Par, Integer> issueMoney(int amount) throws Exception {
        Map<Par, Integer> result = new HashMap<>();
        Iterator<Par> it = cartridgesByPar.keySet().iterator();
        while (amount > 0 && it.hasNext()) {
            Par par = it.next();
            int value = par.getValue();
            int cnt = amount / value;
            cnt = Math.min(cnt, cartridgesByPar.get(par).getCount());
            result.put(par, cnt);
            amount -= cnt * value;
        }
        if (amount != 0) {
            throw new NoChargeException();
        }
        for (var entry : result.entrySet()) {
            Cartridge cartridge = getCartridge(entry.getKey());
            cartridge.take(entry.getValue());
        }
        notifyEvent(new IssueMoneyAtmEvent());
        return result;
    }

    @Override
    public boolean checkHealth() {
        notifyEvent(new HealthCheckAtmEvent());
        return cartridgesByPar.entrySet().stream().allMatch(e -> e.getValue().getCount() > 0);
    }

    @Override
    public String toString() {
        return "AtmImpl{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public MementoAtm makeSnapshot() {
        MementoAtm memento = new MementoAtm();
        memento.setState(new AtmState(cartridgesByPar));
        return memento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void restoreFromSnapshot(Memento memento) {
        AtmState state = (AtmState) memento.getState();
        cartridgesByPar.clear();
        cartridgesByPar.putAll(state.getCartridges());
    }

    private Cartridge getCartridge(Par par) {
        if (!cartridgesByPar.containsKey(par)) {
            throw new IllegalArgumentException("There is no cartridge with par: " + par);
        }
        return cartridgesByPar.get(par);
    }
}
