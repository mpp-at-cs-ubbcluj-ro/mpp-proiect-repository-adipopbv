package pr1Java.persistence.database.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pr1Java.model.User;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.persistence.Configuration;
import pr1Java.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class UserHibernateRepository extends HibernateRepository implements UserRepository {
    public UserHibernateRepository() {
        Configuration.loadProperties("./persistence/persistence.config");
        Configuration.logger.info("Initializing UserHibernateRepository with {} ", Configuration.properties);
    }

    @Override
    public Iterable<User> getAll() {
        Configuration.logger.traceEntry("Getting all users");

        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                users = session.createQuery("select u from User u", User.class).getResultList();
                transaction.commit();
                Configuration.logger.trace("Gotten instances");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(users);
        return users;
    }

    @Override
    public User getOne(String username) throws NotFoundException {
        Configuration.logger.traceEntry("Getting user with {}", username);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                User user = session.createQuery("select u from User u where u.id = " + username + ";", User.class).getSingleResult();
                transaction.commit();
                Configuration.logger.trace("Gotten instance");

                Configuration.logger.traceExit(user);
                return user;
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
                throw new NotFoundException("user not found");
            }
        }
    }

    @Override
    public User add(User user) throws DuplicateException {
        Configuration.logger.traceEntry("Saving {} ", user);

        try {
            getOne(user.getUsername());
            throw new DuplicateException("user already registered");
        } catch (NotFoundException ignored) {
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
                Configuration.logger.trace("Saved instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public User remove(String username) throws NotFoundException {
        Configuration.logger.traceEntry("Removing user with {} ", username);

        User user = getOne(username);
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(user);
                transaction.commit();
                Configuration.logger.trace("Deleted instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(user);
         return user;
    }

    @Override
    public User modify(String username, User newUser) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying user with {} ", username);

        User oldUser = getOne(username);
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(newUser);
                transaction.commit();
                Configuration.logger.trace("Modified instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(oldUser);
        return oldUser;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (User user : getAll())
            string.append(user).append("\n");
        return string.toString();
    }
}
