package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection conn = Util.getConnection();
    private static final String CREATE = """
            CREATE TABLE user (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(20) NOT NULL,
                lastname VARCHAR(20) NOT NULL,
                age TINYINT NOT NULL
            );""";
    private static final String DROP = "DROP TABLE IF EXISTS user;";
    private static final String INSERT = "INSERT INTO user(name, lastname, age) VALUES (?, ?, ?);";
    private static final String DELETE = "DELETE FROM user WHERE id = ?;";
    private static final String SELECT = "SELECT * FROM user;";
    private static final String TRUNCATE = "TRUNCATE TABLE user;";
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE);
            System.out.println("Table User was created");
        } catch (SQLException e) {
            System.err.println("Table User wasn't created");
        }
    }

    public void dropUsersTable() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(DROP);
            System.out.println("Table User was deleted");
        } catch (SQLException e) {
            System.err.println("Table User wasn't deleted");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User with name – %s added in db", name);
            System.out.println();
        } catch (SQLException e) {
            System.err.println("User wasn't saved");
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("User was removed");
        } catch (SQLException e) {
            System.err.println("User wasn't removed");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (ResultSet resultSet = conn.createStatement().executeQuery(SELECT)) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                Byte age = resultSet.getByte("age");
                User user = new User(name, lastname, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user list");
        }
        System.out.println(users);
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(TRUNCATE);
            System.out.println("Table was cleaned");
        } catch (SQLException e) {
            System.err.println("Table wasn't cleaned");
        }
    }
}
