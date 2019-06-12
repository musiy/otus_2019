import atm.Atm;
import atm.AtmFactory;
import par.Par;
import par.ParService;
import par.ParServiceImpl;

import java.util.Map;
import java.util.Scanner;

class AppAtm {

    private ParService parService;

    private AtmFactory atmFactory;

    AppAtm(ParServiceImpl parService) {
        this.parService = parService;
        atmFactory = new AtmFactory(parService);
    }

    private void run() {
        Atm atm = atmFactory.createAtm();
        fillAtm(atm);

        System.out.println("Всего в банкомате: " + atm.getTotalAmount());
        System.out.print("Введите сумму для выдачи (кратно 100) > ");
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();
        try {
            Map<Par, Integer> notes = atm.giveMoney(num);
            System.out.println("Средства успено выданы:");
            for (var entry : notes.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("Невозможно выдать запрошенную сумму: " + e.getMessage());
        } finally {
            System.out.println("Всего в банкомате: " + atm.getTotalAmount());
        }
    }

    private void fillAtm(Atm atm) {
        atm.putMoney(parService.getPar(100), 10);
        atm.putMoney(parService.getPar(200), 10);
        atm.putMoney(parService.getPar(500), 10);
        atm.putMoney(parService.getPar(1000), 10);
        atm.putMoney(parService.getPar(2000), 10);
        atm.putMoney(parService.getPar(5000), 10);
    }

    public static void main(String[] args) {
        ParServiceImpl parServiceImpl = new ParServiceImpl();
        parServiceImpl.init();

        AppAtm appAtm = new AppAtm(parServiceImpl);
        appAtm.run();
    }
}
