import atm.AtmCommon;
import atm.AtmFactory;
import dept.AtmDept;
import dept.AtmDeptFactory;

import java.util.Optional;
import java.util.Scanner;

public class Application {

    private AtmDeptFactory atmDeptFactory = new AtmDeptFactory(new AtmFactory());

    public static void main(String[] args) throws Exception {
        new Application().run();
    }

    void run() throws Exception {
        AtmDept atmDept = atmDeptFactory.create();
        // получим баланс
        System.out.println(atmDept.getBalance());
        // проверим, что банкоматы в рабочем состоянии
        if (atmDept.checkHealth()) {
            System.out.println("All ATMs are in service.");
        } else {
            System.out.println("There are one or more ATM out of service");
        }
        System.out.print("Введите сумму для выдачи (кратно 100) > ");
        Scanner in = new Scanner(System.in);
        int num = in.nextInt();

        Optional<AtmCommon> optionalAtm = atmDept.findAtm("ATM-1");
        if (optionalAtm.isPresent()) {
            var atmCommon = optionalAtm.get();
            System.out.println("Баланс до выдачи: " + atmCommon.getBalance());
            atmCommon.issueMoney(num);
            System.out.println("Баланс после выдачи: " + atmCommon.getBalance());
            atmDept.restoreAtmState(atmCommon);
            System.out.println("Баланс после сброса состояния: " + atmCommon.getBalance());
        }

    }
}
