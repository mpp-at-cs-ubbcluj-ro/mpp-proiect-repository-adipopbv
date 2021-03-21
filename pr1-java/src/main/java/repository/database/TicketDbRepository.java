package repository.database;

import domain.Game;
import domain.Ticket;
import domain.exceptions.DatabaseException;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.TicketRepository;
import utils.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketDbRepository implements TicketRepository {
    public TicketDbRepository() {
        Configuration.logger.info("Initializing TicketDbRepository with properties: {} ", Configuration.properties);
    }

    @Override
    public Iterable<Ticket> getAll() {
        Configuration.logger.traceEntry();

        Connection connection = DbUtils.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from tickets inner join games on games.gameId = tickets.forGameId;")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ticketId");
                    String clientName = result.getString("clientName");

                    Ticket ticket = new Ticket(id, GameDbRepository.getGameFromResultSet(result), clientName);
                    tickets.add(ticket);
                }
            }
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public Ticket getOne(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry();

        Connection connection = DbUtils.getConnection();
        Ticket ticket = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from tickets inner join games on games.gameId = tickets.forGameId where ticketId = " + id + ";")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                String clientName = result.getString("clientName");

                ticket = new Ticket(id, GameDbRepository.getGameFromResultSet(result), clientName);
            } catch (SQLException exception) {
                Configuration.logger.error(exception);
                throw new NotFoundException();
            }
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket add(Ticket ticket) throws DuplicateException {
        Configuration.logger.traceEntry("Saving ticket {} ", ticket);

        try {
            getOne(ticket.getId());
            throw new DuplicateException();
        } catch (NotFoundException ignored) {
        }

        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tickets(ticketId, forGameId, clientName) " + "values(?,?,?);")) {
            preparedStatement.setInt(1, ticket.getId());
            preparedStatement.setInt(2, ticket.getForGame().getId());
            preparedStatement.setString(3, ticket.getClientName());

            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Saved {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket remove(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Removing ticket with id {} ", id);

        Ticket ticket = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from tickets where ticketId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Deleted {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            throw new NotFoundException();
        }

        Configuration.logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket modify(Integer id, Ticket newTicket) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying ticket with id {} ", id);

        Ticket oldTicket = getOne(id);
        Connection connection = DbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update tickets set " +
                "forGameId = " + newTicket.getForGame().getId() +
                ", clientName = '" + newTicket.getClientName() +
                "' where ticketId = " + id + ";")) {
            int result = preparedStatement.executeUpdate();
            Configuration.logger.trace("Modified {} instances", result);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(oldTicket);
        return oldTicket;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Ticket ticket : getAll())
            string.append(ticket).append("\n");
        return string.toString();
    }
}
