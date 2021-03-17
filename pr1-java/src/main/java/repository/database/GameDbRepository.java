package repository.database;

import domain.Game;
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
                    Integer gameId = result.getInt("gameId");
                    String name = result.getString("name");
                    String homeTeam = result.getString("homeTeam");
                    String awayTeam = result.getString("awayTeam");
                    Integer availableSeats = result.getInt("availableSeats");
                    Integer seatCost = result.getInt("seatCost");

                    Game game = new Game(gameId, name, homeTeam, awayTeam, availableSeats, seatCost);
                    games.add(game);
                }
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(games);
        return games;
    }

    @Override
    public Game getOne(Integer id) throws NotFoundException {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        Game game = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from games where gameId = " + id + ";")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                String name = result.getString("name");
                String homeTeam = result.getString("homeTeam");
                String awayTeam = result.getString("awayTeam");
                Integer availableSeats = result.getInt("availableSeats");
                Integer seatCost = result.getInt("seatCost");

                game = new Game(id, name, homeTeam, awayTeam, availableSeats, seatCost);
            } catch (SQLException exception) {
                logger.error(exception);
                throw new NotFoundException();
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(game);
        return game;
    }

    @Override
    public Game add(Game game) throws DuplicateException {
        logger.traceEntry("Saving game {} ", game);

        try {
            getOne(game.getId());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into games(gameId, name, homeTeam, awayTeam, availableSeats, seatCost) " + "values(?,?,?,?,?,?);")) {
            preparedStatement.setInt(1, game.getId());
            preparedStatement.setString(2, game.getName());
            preparedStatement.setString(3, game.getHomeTeam());
            preparedStatement.setString(4, game.getAwayTeam());
            preparedStatement.setInt(5, game.getAvailableSeats());
            preparedStatement.setInt(6, game.getSeatCost());

            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(game);
        return game;
    }

    @Override
    public Game remove(Integer id) throws NotFoundException {
        logger.traceEntry("Removing game with id {} ", id);

        Game game = getOne(id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from games where gameId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new NotFoundException();
        }

        logger.traceExit(game);
        return game;
    }

    @Override
    public Game modify(Integer id, Game newGame) throws NotFoundException {
        logger.traceEntry("Modifying game with id {} ", id);

        Game oldGame = getOne(id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update games set " +
                "name = '" + newGame.getName() +
                "', homeTeam = '" + newGame.getHomeTeam() +
                "', awayTeam = '" + newGame.getAwayTeam() +
                "', availableSeats = " + newGame.getAvailableSeats() +
                ", seatCost = " + newGame.getSeatCost() +
                " where gameId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(oldGame);
        return oldGame;

    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Game game : getAll())
            string.append(game).append("\n");
        return string.toString();
    }
}
