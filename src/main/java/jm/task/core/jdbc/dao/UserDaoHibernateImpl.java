package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    SessionFactory sessionFactory = Util.getSessionFactory();
    private static final String CREATE = """
            CREATE TABLE IF NOT EXISTS user (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(20) NOT NULL,
                lastname VARCHAR(20) NOT NULL,
                age TINYINT NOT NULL
            );""";
    private static final String DROP = "DROP TABLE IF EXISTS user";
    public static final String TRUNCATE = "TRUNCATE TABLE user";
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createNativeQuery(CREATE).executeUpdate();
            System.out.println("Created table User");
        } catch (HibernateException e) {
            System.err.println("Table not created");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createNativeQuery(DROP).executeUpdate();
            System.out.println("Deleted table User");
        } catch (HibernateException e) {
            System.err.println("Table not deleted");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            System.out.printf("User with name – %s added in db", name);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("User wasn't saved");
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
            System.out.println("User removed");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("User wasn't removed");
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return session.createQuery("from User", User.class).getResultList();
        } catch (HibernateException e) {
            System.err.println("Error getting user list");
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(TRUNCATE).executeUpdate();
            transaction.commit();
            System.out.println("Table was cleaned");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Table wasn't cleaned");
        }
    }
}
