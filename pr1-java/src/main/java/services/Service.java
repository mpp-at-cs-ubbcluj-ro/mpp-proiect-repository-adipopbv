package services;

import domain.Game;
import domain.Ticket;
import domain.User;
import domain.exceptions.DuplicateException;
import domain.exceptions.LogInException;
import domain.exceptions.NotFoundException;
import domain.exceptions.ParameterException;
import repository.GameRepository;
import repository.TicketRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

public class Service {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final TicketRepository ticketRepository;

    public Service(UserRepository userRepository, GameRepository gameRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Logs in a user
     *
     * @param username the username of the user to be logged in
     * @return the logged in user
     * @throws NotFoundException if user not found
     * @throws LogInException    if user already logged in
     */
    public User logInUser(String username) throws NotFoundException, LogInException {
        User user = userRepository.getOne(username);
        if (user.getStatus().equals("logged-in"))
            throw new LogInException("user already logged in");
        userRepository.setUserStatus(user.getId(), "logged-in");
        return user;
    }

    /**
     * Logs out a user
     *
     * @param username the username of the user to be logged out
     * @return the logged out user
     * @throws NotFoundException if user not found
     * @throws LogInException    if user already logged out
     */
    public User logOutUser(String username) throws NotFoundException, LogInException {
        User user = userRepository.getOne(username);
        if (user.getStatus().equals("logged-out"))
            throw new LogInException("user already logged out");
        userRepository.setUserStatus(user.getId(), "logged-out");
        return user;
    }

    /**
     * Signs up a user
     *
     * @param username the username of the user to be added
     * @return the signed up user
     * @throws DuplicateException if the user already exists
     */
    public User signUpUser(String username) throws DuplicateException {
        User user = new User(username, "logged-in");
        user = userRepository.add(user);
        return user;
    }

    public Collection<Game> getAllGames() {
        Collection<Game> games = new ArrayList<>();
        for (Game game : gameRepository.getAll())
            games.add(game);
        return games;
    }

    public void sellSeats(Game game, String clientName, Integer seatsCount) throws ParameterException, NotFoundException {
        if (game.getAvailableSeats() < seatsCount)
            throw new ParameterException("not enough seats available");
        gameRepository.setGameAvailableSeats(game.getId(), game.getAvailableSeats() - seatsCount);
        while (seatsCount != 0) {
            try {
                ticketRepository.add(new Ticket(game, clientName));
            } catch (DuplicateException ignored) {
            }
            seatsCount--;
        }
    }

    public Collection<Game> getGamesWithAvailableSeatsDescending() {
        Collection<Game> games = new ArrayList<>();
        for (Game game : gameRepository.getGamesByAvailableSeatsDescending(true))
            if (game.getAvailableSeats() > 0)
                games.add(game);
        return games;
    }
}
