package pr1Java.persistence.database.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pr1Java.model.Game;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.persistence.Configuration;
import pr1Java.persistence.GameRepository;

import java.util.ArrayList;
import java.util.List;

public class GameHibernateRepository extends HibernateRepository implements GameRepository {
    public GameHibernateRepository() {
        Configuration.loadProperties("./persistence/persistence.config");
        Configuration.logger.info("Initializing GameHibernateRepository with {} ", Configuration.properties);
    }

    @Override
    public Iterable<Game> getAll() {
        Configuration.logger.traceEntry("Getting all games");

        List<Game> games = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                games = session.createQuery("select g from Game g", Game.class).getResultList();
                transaction.commit();
                Configuration.logger.trace("Gotten instances");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(games);
        return games;
    }

    @Override
    public Game getOne(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Getting game with {}", id);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Game game = session.createQuery("from Game where id like " + id, Game.class).getSingleResult();
                transaction.commit();
                Configuration.logger.trace("Gotten instance");

                Configuration.logger.traceExit(game);
                return game;
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
                throw new NotFoundException("game not found");
            }
        }
    }

    @Override
    public Game add(Game game) throws DuplicateException {
        Configuration.logger.traceEntry("Saving {} ", game);

        try {
            getOne(game.getId());
            throw new DuplicateException("game already registered");
        } catch (NotFoundException ignored) {
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(game);
                transaction.commit();
                Configuration.logger.trace("Saved instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(game);
        return game;
    }

    @Override
    public Game remove(Integer id) throws NotFoundException {
        Configuration.logger.traceEntry("Removing game with {} ", id);

        Game game = getOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(game);
                transaction.commit();
                Configuration.logger.trace("Deleted instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(game);
        return game;
    }

    @Override
    public Game modify(Integer id, Game newGame) throws NotFoundException {
        Configuration.logger.traceEntry("Modifying game with {} ", id);

        Game oldGame = getOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(newGame);
                transaction.commit();
                Configuration.logger.trace("Modified instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(oldGame);
        return oldGame;
    }

    @Override
    public Iterable<Game> getGamesByAvailableSeatsDescending(Boolean reverse) {
        Configuration.logger.traceEntry();

        List<Game> games = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String statement = "select g from Game g order by availableSeats";
                statement += reverse ? " desc" : "";
                games = session.createQuery(statement, Game.class).getResultList();
                transaction.commit();
                Configuration.logger.trace("Gotten instances");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(games);
        return games;
    }

    @Override
    public Game setGameAvailableSeats(Integer id, Integer availableSeats) throws NotFoundException {
        Configuration.logger.traceEntry();

        Game game = getOne(id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                game.setAvailableSeats(availableSeats);
                session.update(game);
                transaction.commit();
                Configuration.logger.trace("Modified availableSeats in instance");
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
                Configuration.logger.error(exception);
            }
        }

        Configuration.logger.traceExit(game);
        return game;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Game game : getAll())
            string.append(game).append("\n");
        return string.toString();
    }
}
