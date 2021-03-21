package repository.database;

import domain.User;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import repository.UserRepository;
import utils.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDbRepository implements UserRepository {
    public UserDbRepository() {
        Configuration.logger.info("Initializing UserDbRepository with {} ", Configuration.properties);
    }

    @Override
    public Iterable<User> getAll() {
        Configuration.logger.traceEntry("Getting all users");

        Connection connection = DbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (ResultSet result = connection.prepareStatement("select * from users;").executeQuery()) {
            while (result.next()) {
                Integer userId = result.getInt("userId");
                String username = result.getString("username");
                String status = result.getString("status");
                User user = new User(userId, username, status);
                users.add(user);
            }
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(users);
        return users;
    }

    @Override
    public User getOne(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Getting user with {}", id);

        Connection connection = DbUtils.getConnection();
        try (ResultSet result = connection.prepareStatement("select * from users where userId = " + id + ";").executeQuery()) {
            String username = result.getString("username");
            String status = result.getString("status");
            User user = new User(id, username, status);

            Configuration.logger.traceExit(user);
            return user;
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException();
        }
    }

    @Override
    public User getOne(String username) throws NotFoundException {
        Configuration.logger.traceEntry("Getting user with {}", username);

        Connection connection = DbUtils.getConnection();
        try (ResultSet result = connection.prepareStatement("select * from users where username = '" + username + "';").executeQuery()) {
            Integer userId = result.getInt("userId");
            String status = result.getString("status");
            User user = new User(userId, username, status);

            Configuration.logger.traceExit(user);
            return user;
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException();
        }
    }

    @Override
    public User add(User user) throws DuplicateException {
        Configuration.logger.traceEntry("Saving {} ", user);

        try {
            getOne(user.getUsername());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into users(username, status) values(?,?);")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getStatus());

            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        try (ResultSet result = connection.prepareStatement("select userId from users where username = '" + user.getUsername() + "';").executeQuery()) {
            user.setId(result.getInt("userId"));
            Configuration.logger.trace("Got id for {}", user);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public User remove(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Removing user with {} ", id);

        User user = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from users where userId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException();
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public User modify(Integer id, User newUser) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying user with {} ", id);

        User oldUser = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update users set " +
                "username = '" + newUser.getUsername() +
                "', status = '" + newUser.getStatus() +
                "' where userId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(oldUser);
        return oldUser;
    }

    @Override
    public User setUserStatus(Integer id, String status) throws NotFoundException {
        Configuration.logger.traceEntry("Setting status for user with {} ", id);

        User user = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update users set status = '" + status + "' where userId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Status set for {}", user);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (User user : getAll())
            string.append(user).append("\n");
        return string.toString();
    }
}
