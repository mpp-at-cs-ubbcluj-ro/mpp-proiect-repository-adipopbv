package repository.database;

import domain.Game;
import domain.Ticket;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.GameRepository;
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
    private final GameRepository gameRepository;

    public TicketDbRepository(Properties properties, GameRepository gameRepository) {
        logger.info("Initializing TicketDbRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
        this.gameRepository = gameRepository;
    }

    @Override
    public Iterable<Ticket> getAll() {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from tickets;")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    Integer gameId = result.getInt("gameId");
                    Integer cost = result.getInt("cost");

                    Ticket ticket = new Ticket(id, gameRepository.getOne(gameId), cost);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error Database " + ex);
        }

        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public Ticket getOne(Integer id) {
        logger.traceEntry();

        Connection connection = dbUtils.getConnection();
        Ticket ticket = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from tickets where id = " + id + ";")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Integer gameId = result.getInt("gameId");
                    Integer cost = result.getInt("cost");

                    ticket = new Ticket(id, gameRepository.getOne(gameId), cost);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error Database " + ex);
        }

        logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket add(Ticket ticket) throws DuplicateException {
        logger.traceEntry("Saving ticket {} ", ticket);

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tickets(id, gameId, cost) " + "values(?,?,?);")) {
            preparedStatement.setInt(1, ticket.getId());
            preparedStatement.setInt(2, ticket.getGame().getId());
            preparedStatement.setInt(3, ticket.getCost());

            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error Database " + ex);
        }

        logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket remove(Integer id) throws NotFoundException {
        return null;
    }

    @Override
    public Ticket modify(Integer id, Ticket newTicket) throws DuplicateException, NotFoundException {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Ticket ticket : getAll())
            string.append(ticket).append("\n");
        return string.toString();
    }
}
