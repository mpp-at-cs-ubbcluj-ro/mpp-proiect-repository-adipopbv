package repository.database;

import domain.Game;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import repository.GameRepository;
import utils.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDbRepository implements GameRepository {
    public GameDbRepository() {
        Configuration.logger.info("Initializing GameDbRepository with properties: {} ", Configuration.properties);
    }

    public static Game getGameFromResultSet(ResultSet resultSet) throws SQLException {
        Integer gameId = resultSet.getInt("gameId");
        String name = resultSet.getString("name");
        String homeTeam = resultSet.getString("homeTeam");
        String awayTeam = resultSet.getString("awayTeam");
        Integer availableSeats = resultSet.getInt("availableSeats");
        Integer seatCost = resultSet.getInt("seatCost");
        return new Game(gameId, name, homeTeam, awayTeam, availableSeats, seatCost);
    }

    @Override
    public Iterable<Game> getAll() {
        Configuration.logger.traceEntry();

        Connection connection = DbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from games;")) {
            getGamesFromDatabase(games, preparedStatement);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(games);
        return games;
    }

    @Override
    public Game getOne(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry();

        Connection connection = DbUtils.getConnection();
        try (ResultSet result = connection.prepareStatement("select * from games where gameId = " + id + ";").executeQuery()) {
            Game game = getGameFromResultSet(result);

            Configuration.logger.traceExit(game);
            return game;
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException();
        }
    }

    @Override
    public Game add(Game game) throws DuplicateException {
        Configuration.logger.traceEntry("Saving game {} ", game);

        try {
            getOne(game.getId());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into games(gameId, name, homeTeam, awayTeam, availableSeats, seatCost) " + "values(?,?,?,?,?,?);")) {
            preparedStatement.setInt(1, game.getId());
            preparedStatement.setString(2, game.getName());
            preparedStatement.setString(3, game.getHomeTeam());
            preparedStatement.setString(4, game.getAwayTeam());
            preparedStatement.setInt(5, game.getAvailableSeats());
            preparedStatement.setInt(6, game.getSeatCost());

            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(game);
        return game;
    }

    @Override
    public Game remove(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Removing game with id {} ", id);

        Game game = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from games where gameId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException();
        }

        Configuration.logger.traceExit(game);
        return game;
    }

    @Override
    public Game modify(Integer id, Game newGame) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying game with id {} ", id);

        Game oldGame = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update games set " +
                "name = '" + newGame.getName() +
                "', homeTeam = '" + newGame.getHomeTeam() +
                "', awayTeam = '" + newGame.getAwayTeam() +
                "', availableSeats = " + newGame.getAvailableSeats() +
                ", seatCost = " + newGame.getSeatCost() +
                " where gameId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(oldGame);
        return oldGame;

    }

    @Override
    public Iterable<Game> getGamesOrderedByAvailableSeats(Boolean reverse) {
        Configuration.logger.traceEntry();

        Connection connection = DbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        String statement = "select * from games order by availableSeats";
        statement += reverse ? " desc;" : ";";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            getGamesFromDatabase(games, preparedStatement);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(games);
        return games;
    }

    private void getGamesFromDatabase(List<Game> games, PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet result = preparedStatement.executeQuery()) {
            while (result.next()) {
                Game game = getGameFromResultSet(result);
                games.add(game);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Game game : getAll())
            string.append(game).append("\n");
        return string.toString();
    }
}
