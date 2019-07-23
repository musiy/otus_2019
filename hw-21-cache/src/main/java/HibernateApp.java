import cacheservice.DbService;
import cacheservice.DbServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import userspace.Address;
import userspace.Phone;
import userspace.User;

import java.util.ArrayList;
import java.util.List;

public class HibernateApp {

    private final SessionFactory sessionFactory;

    private final DbService dbService;

    public static void main(String[] args) {
        HibernateApp app = new HibernateApp();
        long userId = app.doRun();
        app.doCheck(userId);
    }

    private long doRun() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Address address = new Address();
            address.setStreet("Vavilova");
            address.setBuilding("7c3");
            address.setApartment("44");

            List<Phone> phones = new ArrayList<>();
            phones.add(createPhone("123"));
            phones.add(createPhone("456"));
            phones.add(createPhone("789"));

            User user = new User();
            user.setName("Sebastian Hacker");
            user.setAge(30);
            user.setName("Sebastian Hacker");
            user.setAddress(address);
            user.setPhones(phones);

            dbService.saveOrUpdate(session, user);
            session.getTransaction().commit();
            return user.getId();
        }
    }

    private Phone createPhone(String number) {
        Phone phone = new Phone();
        phone.setNumber(number);
        return phone;
    }

    private void doCheck(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = dbService.get(session, User.class, id);
            System.out.println("Name:" + user.getName());
            System.out.println("Age:" + user.getAge());
            System.out.println("Address: " + user.getAddress().getStreet());
            List<Phone> phones = user.getPhones();
            phones.forEach(ph -> System.out.println(ph.getNumber()));
        }
    }

    private HibernateApp() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(userspace.User.class)
                .addAnnotatedClass(userspace.Address.class)
                .addAnnotatedClass(userspace.Phone.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();

        dbService = new DbServiceImpl();
    }
}
