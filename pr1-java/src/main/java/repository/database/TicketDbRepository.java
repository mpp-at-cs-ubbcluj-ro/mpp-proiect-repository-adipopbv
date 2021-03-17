package repository.database;

import domain.Game;
import domain.Ticket;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.TicketRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketDbRepository implements TicketRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public TicketDbRepository(Properties properties) {
        logger.info("Initializing TicketDbRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public Iterable<Ticket> getAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from tickets inner join games on games.gameId = tickets.forGameId;")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ticketId");
                    String clientName = result.getString("clientName");

                    Ticket ticket = new Ticket(id, getGameFromResultSet(result), clientName);
                    tickets.add(ticket);
                }
            } catch (NotFoundException exception) {
                logger.error(exception);
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public Ticket getOne(Integer id) throws NotFoundException {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        Ticket ticket = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from tickets inner join games on games.gameId = tickets.forGameId where ticketId = " + id + ";")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                String clientName = result.getString("clientName");

                ticket = new Ticket(id, getGameFromResultSet(result), clientName);
            } catch (SQLException exception) {
                logger.error(exception);
                throw new NotFoundException();
            }
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket add(Ticket ticket) throws DuplicateException {
        logger.traceEntry("Saving ticket {} ", ticket);

        try {
            getOne(ticket.getId());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tickets(ticketId, forGameId, clientName) " + "values(?,?,?);")) {
            preparedStatement.setInt(1, ticket.getId());
            preparedStatement.setInt(2, ticket.getForGame().getId());
            preparedStatement.setString(3, ticket.getClientName());

            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket remove(Integer id) throws NotFoundException {
        logger.traceEntry("Removing ticket with id {} ", id);

        Ticket ticket = getOne(id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from tickets where ticketId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new NotFoundException();
        }

        logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket modify(Integer id, Ticket newTicket) throws NotFoundException {
        logger.traceEntry("Modifying ticket with id {} ", id);

        Ticket oldTicket = getOne(id);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update tickets set " +
                "forGameId = " + newTicket.getForGame().getId() +
                ", clientName = '" + newTicket.getClientName() +
                "' where ticketId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            logger.error(exception);
        }

        logger.traceExit(oldTicket);
        return oldTicket;
    }

    private Game getGameFromResultSet(ResultSet result) throws NotFoundException {
        try {
            Integer gameId = result.getInt("gameId");
            String name = result.getString("name");
            String homeTeam = result.getString("homeTeam");
            String awayTeam = result.getString("awayTeam");
            Integer availableSeats = result.getInt("availableSeats");
            Integer seatCost = result.getInt("seatCost");
            return new Game(gameId, name, homeTeam, awayTeam, availableSeats, seatCost);
        } catch (SQLException exception) {
            logger.error(exception);
            throw new NotFoundException();
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Ticket ticket : getAll())
            string.append(ticket).append("\n");
        return string.toString();
    }
}
