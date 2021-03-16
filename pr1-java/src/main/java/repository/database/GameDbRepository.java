package repository.database;

import domain.Game;
import domain.User;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.GameRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameDbRepository implements GameRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public GameDbRepository(Properties properties) {
        logger.info("Initializing GameBdRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Iterable<Game> getAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from games;")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String name = result.getString("name");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Integer availableSeats = result.getInt("availableSeats");

                    Game game = new Game(id, name, homeTeam, awayTeam, availableSeats);
                    games.add(game);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error Database " + ex);
        }

        logger.traceExit(games);
        return games;
    }

    @Override
    public Game getOne(Integer id) {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        Game game = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from games where id = " + id + ";")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    String name = result.getString("name");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Integer availableSeats = result.getInt("availableSeats");

                    game = new Game(id, name, homeTeam, awayTeam, availableSeats);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error Database " + ex);
        }

        logger.traceExit(game);
        return game;
    }

    @Override
    public Game add(Game game) throws DuplicateException {
        logger.traceEntry("Saving game {} ", game);

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into games(id, name, homeTeam, awayTeam, availableSeats) " + "values(?,?,?,?,?);")) {
            preparedStatement.setInt(1, game.getId());
            preparedStatement.setString(2, game.getName());
            preparedStatement.setString(3, game.getHomeTeam());
            preparedStatement.setString(4, game.getAwayTeam());
            preparedStatement.setInt(5, game.getAvailableSeats());

            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error Database " + ex);
        }

        logger.traceExit(game);
        return game;
    }

    @Override
    public Game remove(Integer id) throws NotFoundException {
        return null;
    }

    @Override
    public Game modify(Integer id, Game newGame) throws DuplicateException, NotFoundException {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Game game : getAll())
            string.append(game).append("\n");
        return string.toString();
    }
}
