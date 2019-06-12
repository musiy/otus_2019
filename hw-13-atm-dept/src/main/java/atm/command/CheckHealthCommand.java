package atm.command;

import atm.Atm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckHealthCommand implements AtmCommand {

    private  List<Atm> outOfService = new ArrayList<>();

    @Override
    public void run(Atm atm) {
        if (!atm.checkHealth()) {
            outOfService.add(atm);
        }
    }

    public List<Atm> getOutOfService() {
        return Collections.unmodifiableList(outOfService);
    }
}
