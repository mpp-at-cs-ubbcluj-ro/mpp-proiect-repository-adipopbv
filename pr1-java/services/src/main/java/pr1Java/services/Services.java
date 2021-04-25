package pr1Java.services;

import pr1Java.model.Game;
import pr1Java.model.Ticket;
import pr1Java.model.User;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.ParameterException;
import pr1Java.model.exceptions.SignInException;
import pr1Java.model.observers.IObserver;
import pr1Java.persistence.GameRepository;
import pr1Java.persistence.TicketRepository;
import pr1Java.persistence.UserRepository;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Services implements IServices {

    private final Integer defaultThreadsCount = 5;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final TicketRepository ticketRepository;
    private final Map<String, IObserver> signedInClients;

    public Services(UserRepository userRepository, GameRepository gameRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.ticketRepository = ticketRepository;
        signedInClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User signInUser(String username, String password, IObserver client) throws Exception {
        Configuration.logger.traceEntry("entering with {} {} {}", username, password, client);

        if (signedInClients.containsKey(username))
            throw new SignInException("user already signed in");
        User user = userRepository.getOne(username);
        if (!user.getPassword().equals(password))
            throw new SignInException("incorrect password");
        signedInClients.put(user.getUsername(), client);
        Configuration.logger.trace("user {} signed in", user);

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public synchronized void signOutUser(String username, IObserver client) throws Exception {
        Configuration.logger.traceEntry("entering with {} {}", username, client);

        if (!signedInClients.containsKey(username))
            throw new SignInException("user already signed out");
        User user = userRepository.getOne(username);
        signedInClients.remove(user.getUsername());
        Configuration.logger.trace("user {} signed out", user);

        Configuration.logger.traceExit();
    }

    @Override
    public synchronized User signUpUser(String username, String password, IObserver client) throws Exception {
        Configuration.logger.traceEntry("entering with {} {} {}", username, password, client);

        User user = userRepository.add(new User(username, password));
        Configuration.logger.trace("user {} registered", user);
        signInUser(username, password, client);

        Configuration.logger.traceExit(user);
        return user;
    }

    @Override
    public synchronized Collection<Game> getAllGames() {
        Configuration.logger.traceEntry();

        Collection<Game> games = new ArrayList<>();
        for (Game game : gameRepository.getAll())
            games.add(game);

        Configuration.logger.traceExit(games);
        return games;
    }

    @Override
    public synchronized void sellSeats(Game game, String clientName, Integer seatsCount) throws Exception {
        Configuration.logger.traceEntry("entering with {} {} {}", game, clientName, seatsCount);

        if (game.getAvailableSeats() < seatsCount)
            throw new ParameterException("not enough seats available");
        gameRepository.setGameAvailableSeats(game.getId(), game.getAvailableSeats() - seatsCount);
        game.setAvailableSeats(game.getAvailableSeats() - seatsCount);
        notifySeatsSold(game.getId(), seatsCount);
        Configuration.logger.trace("updated available seats {}", game.getAvailableSeats() - seatsCount);
        while (seatsCount != 0) {
            try {
                ticketRepository.add(new Ticket(game, clientName));
            } catch (DuplicateException ignored) {
            }
            seatsCount--;
        }
        Configuration.logger.trace("added tickets");

        Configuration.logger.traceExit();
    }

    private void notifySeatsSold(Integer gameId, Integer seatsCount) {
        Configuration.logger.traceEntry();

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsCount);
        for (IObserver client : signedInClients.values()) {
            executor.execute(() -> {
                try {
                    client.seatsSold(gameId, seatsCount);
                } catch (RemoteException e) {
                    Configuration.logger.error("unable to notify seats sold", e);
                }
            });
        }
        executor.shutdown();
        Configuration.logger.trace("notified clients of seats sold");

        Configuration.logger.traceExit();
    }

    @Override
    public synchronized Collection<Game> getGamesWithAvailableSeatsDescending() {
        Configuration.logger.traceEntry();

        Collection<Game> games = new ArrayList<>();
        for (Game game : gameRepository.getGamesByAvailableSeatsDescending(true))
            if (game.getAvailableSeats() > 0)
                games.add(game);

        Configuration.logger.traceExit();
        return games;
    }
}
