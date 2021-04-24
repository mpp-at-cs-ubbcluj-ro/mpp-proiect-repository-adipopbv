package pr1Java.services.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import pr1Java.model.Game;
import pr1Java.model.Ticket;
import pr1Java.model.User;
import pr1Java.model.exceptions.thrift.*;
import pr1Java.services.thrift.datatransfer.ConnectionInfo;
import pr1Java.services.thrift.datatransfer.DtoUtils;
import pr1Java.services.thrift.datatransfer.GameDto;
import pr1Java.services.thrift.datatransfer.UserDto;
import pr1Java.persistence.GameRepository;
import pr1Java.persistence.TicketRepository;
import pr1Java.persistence.UserRepository;
import pr1Java.services.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThriftHandler implements ThriftServices.Iface {

    private final Integer defaultThreadsCount = 5;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final TicketRepository ticketRepository;
    private final Map<String, ConnectionInfo> signedInClients;

    public ThriftHandler(UserRepository userRepository, GameRepository gameRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.ticketRepository = ticketRepository;
        signedInClients = new HashMap<>();
    }

    @Override
    public UserDto signInUser(String username, String password, ConnectionInfo connectionInfo) throws TException {
        Configuration.logger.traceEntry("entering with {} {}", username, password);

        if (signedInClients.containsKey(username))
            throw new SignInException("user already signed in");
        User user = null;
        try {
            user = userRepository.getOne(username);
        } catch (pr1Java.model.exceptions.NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        if (!user.getPassword().equals(password))
            throw new SignInException("incorrect password");
        signedInClients.put(user.getUsername(), connectionInfo);
        Configuration.logger.trace("user {} signed in", user);

        Configuration.logger.traceExit(user);
        return DtoUtils.toDto(user);
    }

    @Override
    public void signOutUser(String username) throws TException {
        Configuration.logger.traceEntry("entering with {}", username);

        if (!signedInClients.containsKey(username))
            throw new SignInException("user already signed out");
        User user = null;
        try {
            user = userRepository.getOne(username);
        } catch (pr1Java.model.exceptions.NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        signedInClients.remove(user.getUsername());
        Configuration.logger.trace("user {} signed out", user);

        Configuration.logger.traceExit();
    }

    @Override
    public UserDto signUpUser(String username, String password, ConnectionInfo connectionInfo) throws TException {
        Configuration.logger.traceEntry("entering with {} {}", username, password);

        User user = null;
        try {
            user = userRepository.add(new User(username, password));
        } catch (pr1Java.model.exceptions.DuplicateException e) {
            throw new DuplicateException(e.getMessage());
        }
        Configuration.logger.trace("user {} registered", user);
        signInUser(username, password, connectionInfo);

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
            throw new ParameterException("not enough seats available");
        try {
            gameRepository.setGameAvailableSeats(game.getGameId(), game.getAvailableSeats() - seatsCount);
        } catch (pr1Java.model.exceptions.NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        notifySeatsSold(game.getGameId(), seatsCount);
        Configuration.logger.trace("updated available seats {}", game.getAvailableSeats() - seatsCount);
        while (seatsCount != 0) {
            try {
                ticketRepository.add(new Ticket(DtoUtils.toGame(game), clientName));
            } catch (pr1Java.model.exceptions.DuplicateException ignored) {
            }
            seatsCount--;
        }
        Configuration.logger.trace("added tickets");

        Configuration.logger.traceExit();
    }

    private void notifySeatsSold(Integer gameId, Integer seatsCount) throws TTransportException {
        Configuration.logger.traceEntry();

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsCount);
        for (ConnectionInfo connectionInfo : signedInClients.values()) {
            TTransport connection = new TSocket(connectionInfo.ipAddress, connectionInfo.port);
            connection.open();
            var client = new ThriftClient.Client(new TBinaryProtocol(connection));

            executor.execute(() -> {
                try {
                    client.seatsSold(gameId, seatsCount);
                } catch (TException e) {
                    e.printStackTrace();
                }
            });

            connection.close();
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
