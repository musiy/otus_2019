import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import user_space.Address;
import user_space.Phone;
import user_space.User;

import java.util.List;

public class HibernateApp {

    private SessionFactory sessionFactory;

    public static void main(String[] args) {
        HibernateApp app = new HibernateApp();
        long userId = app.doRun();
        app.doCheck(userId);
    }

    private long doRun() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = new User();
            user.setName("Sebastian Hacker");
            user.setAge(30);
            session.saveOrUpdate(user);

            Address address = new Address();
            address.setStreet("Vavilova");
            address.setBuilding("7c3");
            address.setApartment("44");
            address.setUser(user);
            session.saveOrUpdate(address);

            session.saveOrUpdate(createPhone("123", user));
            session.saveOrUpdate(createPhone("456", user));
            session.saveOrUpdate(createPhone("789", user));

            session.getTransaction().commit();
            return user.getId();
        }
    }

    private Phone createPhone(String number, User user) {
        Phone phone = new Phone();
        phone.setNumber(number);
        phone.setUser(user);
        return phone;
    }

    private void doCheck(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            System.out.println("Name:" + user.getName());
            System.out.println("Age:" + user.getAge());
            System.out.println("Address: " + user.getAddress().getStreet());
            List<Phone> phones = user.getPhones();
            phones.forEach(ph -> System.out.println(ph.getNumber()));

        }
    }

    private HibernateApp() {
        init();
    }

    private void init() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(user_space.User.class)
                .addAnnotatedClass(user_space.Address.class)
                .addAnnotatedClass(user_space.Phone.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }
}
