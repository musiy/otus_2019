import engine.JdbcTemplate;
import engine.JdbcTemplateImpl;
import user_space.Account;
import user_space.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppJdbcTemplate {

    private static final String H2_CONNECTION_URL = "jdbc:h2:mem:";

    private Connection connection;

    public static void main(String[] args) throws Exception {
        new AppJdbcTemplate().run();
    }

    private AppJdbcTemplate() throws SQLException {
        this.connection = DriverManager.getConnection(H2_CONNECTION_URL);
        this.connection.setAutoCommit(false);
    }

    private void run() throws Exception {
        createTableUser(connection);
        createTableAccount(connection);
        testUser();
        testAccount();
    }

    private void testUser() throws SQLException {
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplateImpl<>(connection, User.class);
        User user = newUser();
        jdbcTemplate.create(user);
        user.setAge(31);
        jdbcTemplate.update(user);
        User user2 = jdbcTemplate.load(user.getId());
        System.out.println(user.equals(user2));
    }

    private void testAccount() throws SQLException {
        JdbcTemplate<Account> jdbcTemplate = new JdbcTemplateImpl<>(connection, Account.class);
        Account account = newAccount();
        jdbcTemplate.create(account);
        account.setRest(account.getRest() + 1);
        jdbcTemplate.update(account);
        Account account2 = jdbcTemplate.load(account.getNo());
        System.out.println(account.equals(account2));
    }

    private User newUser() {
        User user = new User();
        user.setName("Sebastian");
        user.setAge(30);
        return user;
    }

    private Account newAccount() {
        Account account = new Account();
        account.setRest(100);
        account.setType("LEGAL");
        return account;
    }

    private void createTableUser(Connection connection) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50), age int)")) {
            pst.executeUpdate();
        }
    }

    private void createTableAccount(Connection connection) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        }
    }
}
