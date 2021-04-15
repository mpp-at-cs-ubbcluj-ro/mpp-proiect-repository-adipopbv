package pr1Java.networking.reflection;

import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.model.exceptions.NetworkingException;
import pr1Java.networking.Configuration;
import pr1Java.networking.reflection.datatransfer.*;
import pr1Java.model.observers.IObserver;
import pr1Java.services.reflection.ReflectionServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesRpcProxy implements ReflectionServices {

    private final String host;
    private final Integer port;
    private final LinkedBlockingQueue<Response> responses;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Socket connection = null;
    private volatile boolean finished;
    private IObserver client;

    public ServicesRpcProxy(String host, Integer port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
    }

    private void ensureConnection() throws NetworkingException {
        if (connection != null && input != null && output != null)
            return;

        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            Thread thread = new Thread(new ReaderThread());
            thread.start();
            Configuration.logger.trace("ensured connection {} {} {}", connection, input, output);
        } catch (IOException exception) {
            throw new NetworkingException("could not ensure connection");
        }
    }

    private void closeConnection() throws NetworkingException {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
            Configuration.logger.trace("closed connection");
        } catch (IOException exception) {
            throw new NetworkingException("could not close connection");
        }

    }

    private void sendRequest(Request request) throws NetworkingException {
        ensureConnection();
        try {
            output.writeObject(request);
            output.flush();
            Configuration.logger.trace("sent request {}", request);
        } catch (IOException exception) {
            throw new NetworkingException("error sending object " + exception);
        }
    }

    private Response readResponse() throws NetworkingException {
        ensureConnection();
        Response response = null;
        try {
            response = responses.take();
            Configuration.logger.trace("taken response {} for reading", response);
        } catch (InterruptedException exception) {
            throw new NetworkingException("error reading object " + exception);
        }
        return response;
    }

    @Override
    public User signInUser(String username, String password, IObserver client) throws Exception {
        Configuration.logger.traceEntry("entering with {} {} {}", username, password, client);

        User user = new User(username, password);
        UserDto userDto = DtoUtils.toDto(user);

        Request request = new Request.Builder().setType(RequestType.SIGN_IN).setData(userDto).build();
        sendRequest(request);

        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            this.client = client;
            user = DtoUtils.toUser((UserDto) response.getData());

            Configuration.logger.traceExit(user);
            return user;
        } else if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getData().toString());
        } else {
            throw new NetworkingException("received wrong response " + response.getType());
        }
    }

    @Override
    public void signOutUser(String username, IObserver client) throws Exception {
        Configuration.logger.traceEntry("entering with {} {}", username, client);

        UsernameDto usernameDto = DtoUtils.toDto(username);

        Request request = new Request.Builder().setType(RequestType.SIGN_OUT).setData(usernameDto).build();
        sendRequest(request);

        Response response = readResponse();
        closeConnection();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getData().toString());
        }

        Configuration.logger.traceExit();
    }

    @Override
    public User signUpUser(String username, String password, IObserver client) throws Exception {
        Configuration.logger.traceEntry("entering with {} {} {}", username, password, client);

        User user = new User(username, password);
        UserDto userDto = DtoUtils.toDto(user);

        Request request = new Request.Builder().setType(RequestType.SIGN_UP).setData(userDto).build();
        sendRequest(request);

        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            this.client = client;
            user = DtoUtils.toUser((UserDto) response.getData());

            Configuration.logger.traceExit(user);
            return user;
        } else if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getData().toString());
        } else {
            throw new NetworkingException("received wrong response " + response.getType());
        }
    }

    @Override
    public Collection<Game> getAllGames() throws Exception {
        Configuration.logger.traceEntry();

        Request request = new Request.Builder().setType(RequestType.GET_ALL_GAMES).build();
        sendRequest(request);

        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            Collection<Game> games = DtoUtils.toGameCollection((GameCollectionDto) response.getData());

            Configuration.logger.traceExit(games);
            return games;
        } else if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getData().toString());
        } else {
            throw new NetworkingException("received wrong response " + response.getType());
        }
    }

    @Override
    public void sellSeats(Game game, String clientName, Integer seatsCount) throws Exception {
        Configuration.logger.traceEntry("entering with {} {} {}", game, clientName, seatsCount);

        SeatsSellingDto seatsSellingDto = DtoUtils.toDto(game, clientName, seatsCount);

        Request request = new Request.Builder().setType(RequestType.SELL_SEATS).setData(seatsSellingDto).build();
        sendRequest(request);

        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getData().toString());
        }

        Configuration.logger.traceExit();
    }

    @Override
    public Collection<Game> getGamesWithAvailableSeatsDescending() throws Exception {
        Configuration.logger.traceEntry();

        Request request = new Request.Builder().setType(RequestType.GET_GAMES_WITH_AVAILABLE_SEATS_DESCENDING).build();
        sendRequest(request);

        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            Collection<Game> games = DtoUtils.toGameCollection((GameCollectionDto) response.getData());

            Configuration.logger.traceExit(games);
            return games;
        } else if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getData().toString());
        } else {
            throw new NetworkingException("received wrong response " + response.getType());
        }
    }

    private void handleResponse(Response response) throws Exception {
        Configuration.logger.traceEntry("entering with {}", response);

        if (response.getType() == ResponseType.OK ||
                response.getType() == ResponseType.ERROR) {
            responses.put(response);

            Configuration.logger.traceExit();
            return;
        }

        String handlerName = "handle" + response.getType();
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Response.class);
            method.invoke(this, response);

            Configuration.logger.traceExit();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
            throw new NetworkingException("unknown response");
        }
    }

    private void handleSEATS_SOLD(Response response) {
        Configuration.logger.traceEntry("entering with {}", response);

        SeatsSoldDto seatsSoldDto = (SeatsSoldDto) response.getData();
        client.seatsSold(seatsSoldDto.getGameId(), seatsSoldDto.getSeatsCount());

        Configuration.logger.traceExit();
    }

//    private boolean isUpdate(Response response) {
//        return response.getType() == ResponseType.SEATS_SOLD;
//    }

//    private void handleUpdate(Response response) {
//        if (response.getType() == ResponseType.SEATS_SOLD) {
//            Game game = DtoUtils.toGame((pr1Java.networking.thrift.dataTransfer.GameDto) response.getData());
//            client.seatsSold(game);
//        }
//    }

    private class ReaderThread implements Runnable {

        public void run() {
            Configuration.logger.traceEntry();

            while (!finished) {
                try {
                    ensureConnection();
                    Configuration.logger.trace("waiting response");
                    Response response = (Response) input.readObject();
                    Configuration.logger.trace("response received {}", response);
                    handleResponse(response);
                    Configuration.logger.trace("response handled {}", response);
//                    Object response = input.readObject();
//                    if (isUpdate((Response) response)) {
//                        handleUpdate((Response) response);
//                    } else {
//                        responses.put((Response) response);
//                    }
                } catch (Exception exception) {
                    Configuration.logger.error(exception);
                }
            }

            Configuration.logger.traceExit();
        }
    }
}
