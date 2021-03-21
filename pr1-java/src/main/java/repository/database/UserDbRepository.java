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
        Configuration.logger.info("Initializing UserDbRepository with properties: {} ", Configuration.properties);
    }

    @Override
    public Iterable<User> getAll() {
        Configuration.logger.traceEntry();

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
        Configuration.logger.traceEntry();

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
    public User add(User user) throws DuplicateException {
        Configuration.logger.traceEntry("Saving user {} ", user);

        try {
            getOne(user.getId());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into users(userId, username, status) values(?,?,?);")) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getStatus());

            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public User remove(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Removing user with id {} ", id);

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
        Configuration.logger.traceEntry("Modifying user with id {} ", id);

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
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (User user : getAll())
            string.append(user).append("\n");
        return string.toString();
    }
}
