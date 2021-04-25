package pr1Java.persistence.database.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pr1Java.model.Ticket;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.persistence.Configuration;
import pr1Java.persistence.TicketRepository;

import java.util.ArrayList;
import java.util.List;

public class TicketHibernateRepository extends HibernateRepository implements TicketRepository {
    public TicketHibernateRepository() {
        Configuration.loadProperties("./persistence/persistence.config");
        Configuration.logger.info("Initializing TicketHibernateRepository with {} ", Configuration.properties);
    }

    @Override
    public Iterable<Ticket> getAll() {
        Configuration.logger.traceEntry("Getting all tickets");

        List<Ticket> tickets = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                tickets = session.createQuery("select t from Ticket t", Ticket.class).getResultList();
                transaction.commit();
                Configuration.logger.trace("Gotten instances");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public Ticket getOne(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Getting ticket with {}", id);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Ticket ticket = session.createQuery("select t from Ticket t where t.id = " + id, Ticket.class).getSingleResult();
                transaction.commit();
                Configuration.logger.trace("Gotten instance");

                Configuration.logger.traceExit(ticket);
                return ticket;
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
                throw new NotFoundException("ticket not found");
            }
        }
    }

    @Override
    public Ticket add(Ticket ticket) throws DuplicateException {
        Configuration.logger.traceEntry("Saving {} ", ticket);

        try {
            getOne(ticket.getId());
            throw new DuplicateException("ticket already registered");
        } catch (NotFoundException ignored) {
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(ticket);
                transaction.commit();
                Configuration.logger.trace("Saved instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket remove(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Removing ticket with {} ", id);

        Ticket ticket = getOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(ticket);
                transaction.commit();
                Configuration.logger.trace("Deleted instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(ticket);
        return ticket;
    }

    @Override
    public Ticket modify(Integer id, Ticket newTicket) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying ticket with {} ", id);

        Ticket oldTicket = getOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(newTicket);
                transaction.commit();
                Configuration.logger.trace("Modified instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
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
