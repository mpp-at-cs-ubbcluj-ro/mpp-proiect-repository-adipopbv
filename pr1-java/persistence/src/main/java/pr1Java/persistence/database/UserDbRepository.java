package pr1Java.persistence.database;

import pr1Java.model.User;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.persistence.Configuration;
import pr1Java.persistence.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDbRepository implements UserRepository {
    public UserDbRepository() {
        Configuration.loadProperties("./persistence/persistence.config");
        Configuration.logger.info("Initializing UserDbRepository with {} ", Configuration.properties);
    }

    @Override
    public Iterable<User> getAll() {
        Configuration.logger.traceEntry("Getting all users");

        Connection connection = DbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (ResultSet result = connection.prepareStatement("select * from users;").executeQuery()) {
            while (result.next()) {
                String username = result.getString("username");
                String password = result.getString("password");
                User user = new User(username, password);
                users.add(user);
            }
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(users);
        return users;
    }

    @Override
    public User getOne(String username) throws NotFoundException {
        Configuration.logger.traceEntry("Getting user with {}", username);

        Connection connection = DbUtils.getConnection();
        try (ResultSet result = connection.prepareStatement("select * from users where username = '" + username + "';").executeQuery()) {
            String password = result.getString("password");
            User user = new User(username, password);

            Configuration.logger.traceExit(user);
            return user;
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException("user not found");
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

        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into users(username, password) values(?,?);")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public User remove(String username) throws NotFoundException {
        Configuration.logger.traceEntry("Removing user with {} ", username);

        User user = getOne(username);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from users where username = '" + username + "';")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException("user not registered");
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public User modify(String username, User newUser) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying user with {} ", username);

        User oldUser = getOne(username);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update users set " +
                "password = '" + newUser.getPassword() +
                "' where username = " + username + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
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
