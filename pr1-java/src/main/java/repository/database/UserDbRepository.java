package repository.database;

import domain.User;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDbRepository implements UserRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserDbRepository(Properties properties) {
        logger.info("Initializing UserBdRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Iterable<User> getAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from users;")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Integer userId = result.getInt("userId");
                    String username = result.getString("username");
                    String status = result.getString("status");

                    User user = new User(userId, username, status);
                    users.add(user);
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(users);
        return users;
    }

    @Override
    public User getOne(Integer id) throws NotFoundException {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from users where userId = " + id + ";")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                String username = result.getString("username");
                String status = result.getString("status");

                user = new User(id, username, status);
            } catch (SQLException exception) {
                logger.error(exception);
                throw new NotFoundException();
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(user);
        return user;
    }

    @Override
    public User add(User user) throws DuplicateException {
        logger.traceEntry("Saving user {} ", user);

        try {
            getOne(user.getId());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into users(userId, username, status) values(?,?,?);")) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getStatus());

            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(user);
        return user;
    }

    @Override
    public User remove(Integer id) throws NotFoundException {
        logger.traceEntry("Removing user with id {} ", id);

        User user = getOne(id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from users where userId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new NotFoundException();
        }

        logger.traceExit(user);
        return user;
    }

    @Override
    public User modify(Integer id, User newUser) throws NotFoundException {
        logger.traceEntry("Modifying user with id {} ", id);

        User oldUser = getOne(id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update users set " +
                "username = '" + newUser.getUsername() +
                "', status = '" + newUser.getStatus() +
                "' where userId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(oldUser);
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
