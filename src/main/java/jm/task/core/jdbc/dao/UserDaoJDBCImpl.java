package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection conn = Util.getConnection();
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
        try (Statement statement = conn.createStatement()) {
            statement.execute(CREATE);
            System.out.println("Table User was created");
        } catch (SQLException e) {
            System.err.println("Table User wasn't created");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.execute(DROP);
            System.out.println("Table User was deleted");
        } catch (SQLException e) {
            System.err.println("Table User wasn't deleted");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT)) {
            conn.setAutoCommit(false);
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
                conn.commit();
                System.out.printf("User with name – %s added in db", name);
                System.out.println();
            } catch (SQLException e) {
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("User wasn't saved");
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(DELETE)) {
            conn.setAutoCommit(false);
            try {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                conn.commit();
                System.out.println("User was removed");
            } catch (SQLException e) {
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("User wasn't removed");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            try {
                ResultSet resultSet = statement.executeQuery(SELECT);
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String lastname = resultSet.getString("lastname");
                    Byte age = resultSet.getByte("age");
                    User user = new User(name, lastname, age);
                    user.setId(id);
                    users.add(user);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error getting user list");
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            try {
                statement.executeUpdate(TRUNCATE);
                conn.commit();
                System.out.println("Table was cleaned");
            } catch (SQLException e) {
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Table wasn't cleaned");
        }
    }
}
