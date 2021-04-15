package pr1Java.services.thrift;

import org.apache.thrift.TException;
import pr1Java.model.Game;
import pr1Java.model.Ticket;
import pr1Java.model.User;
import pr1Java.model.exceptions.DuplicateException;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.services.thrift.datatransfer.DtoUtils;
import pr1Java.services.thrift.datatransfer.GameDto;
import pr1Java.services.thrift.datatransfer.UserDto;
import pr1Java.persistence.GameRepository;
import pr1Java.persistence.TicketRepository;
import pr1Java.persistence.UserRepository;
import pr1Java.services.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThriftHandler implements ThriftServices.Iface {

    private final Integer defaultThreadsCount = 5;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final TicketRepository ticketRepository;
    private final List<String> signedInUsernames;

    public ThriftHandler(UserRepository userRepository, GameRepository gameRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.ticketRepository = ticketRepository;
        signedInUsernames = new ArrayList<>();
    }

    @Override
    public UserDto signInUser(String username, String password) throws TException {
        Configuration.logger.traceEntry("entering with {} {}", username, password);

        if (signedInUsernames.contains(username))
            throw new TException("user already signed in");
        User user = null;
        try {
            user = userRepository.getOne(username);
        } catch (NotFoundException e) {
            throw new TException(e.getMessage());
        }
        if (!user.getPassword().equals(password))
            throw new TException("incorrect password");
        signedInUsernames.add(user.getUsername());
        Configuration.logger.trace("user {} signed in", user);

        Configuration.logger.traceExit(user);
        return DtoUtils.toDto(user);
    }

    @Override
    public void signOutUser(String username) throws TException {
        Configuration.logger.traceEntry("entering with {}", username);

        if (!signedInUsernames.contains(username))
            throw new TException("user already signed out");
        User user = null;
        try {
            user = userRepository.getOne(username);
        } catch (NotFoundException e) {
            throw new TException(e.getMessage());
        }
        signedInUsernames.remove(user.getUsername());
        Configuration.logger.trace("user {} signed out", user);

        Configuration.logger.traceExit();
    }

    @Override
    public UserDto signUpUser(String username, String password) throws TException {
        Configuration.logger.traceEntry("entering with {} {}", username, password);

        User user = null;
        try {
            user = userRepository.add(new User(username, password));
        } catch (DuplicateException e) {
            throw new TException(e.getMessage());
        }
        Configuration.logger.trace("user {} registered", user);
        signInUser(username, password);

        Configuration.logger.traceExit(user);
        return DtoUtils.toDto(user);
    }

    @Override
    public List<GameDto> getAllGames() throws TException {
        Configuration.logger.traceEntry();

        List<GameDto> gameDtos = new ArrayList<>();
        for (Game game : gameRepository.getAll())
            gameDtos.add(DtoUtils.toDto(game));

        Configuration.logger.traceExit(gameDtos);
        return gameDtos;
    }

    @Override
    public void sellSeats(GameDto game, String clientName, int seatsCount) throws TException {
        Configuration.logger.traceEntry("entering with {} {} {}", game, clientName, seatsCount);

        if (game.getAvailableSeats() < seatsCount)
            throw new TException("not enough seats available");
        try {
            gameRepository.setGameAvailableSeats(game.getGameId(), game.getAvailableSeats() - seatsCount);
        } catch (NotFoundException e) {
            throw new TException(e.getMessage());
        }
        notifySeatsSold(game.getGameId(), seatsCount);
        Configuration.logger.trace("updated available seats {}", game.getAvailableSeats() - seatsCount);
        while (seatsCount != 0) {
            try {
                ticketRepository.add(new Ticket(DtoUtils.toGame(game), clientName));
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
        for (String username : signedInUsernames) {
            executor.execute(() -> {
//                client.seatsSold(gameId, seatsCount);
            });
        }
        executor.shutdown();
        Configuration.logger.trace("notified clients of seats sold");

        Configuration.logger.traceExit();
    }

    @Override
    public List<GameDto> getGamesWithAvailableSeatsDescending() throws TException {
        Configuration.logger.traceEntry();

        List<GameDto> gameDtos = new ArrayList<>();
        for (Game game : gameRepository.getGamesByAvailableSeatsDescending(true))
            if (game.getAvailableSeats() > 0)
                gameDtos.add(DtoUtils.toDto(game));

        Configuration.logger.traceExit();
        return gameDtos;
    }
}
