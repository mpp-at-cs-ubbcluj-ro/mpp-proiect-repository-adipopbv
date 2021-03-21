package services;

import domain.User;
import domain.exceptions.DuplicateException;
import domain.exceptions.LogInException;
import domain.exceptions.NotFoundException;
import repository.GameRepository;
import repository.TicketRepository;
import repository.UserRepository;

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
     * @throws LogInException if user already logged in
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
     * @throws LogInException if user already logged out
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
}
