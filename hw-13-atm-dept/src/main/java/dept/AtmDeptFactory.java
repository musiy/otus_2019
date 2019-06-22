package dept;

import atm.AtmFactory;

public class AtmDeptFactory {

    private AtmFactory atmFactory;

    public AtmDeptFactory(AtmFactory atmFactory) {
        this.atmFactory = atmFactory;
    }

    public AtmDept create() throws Exception {
        return AtmDept.Builder.create()
                .addAtm(atmFactory.createAtm())
                .addAtm(atmFactory.createAtm())
                .addAtm(atmFactory.createAtm())
                .addAtm(atmFactory.createAtm())
                .addAtm(atmFactory.createAtm())
                .build();
    }
}
