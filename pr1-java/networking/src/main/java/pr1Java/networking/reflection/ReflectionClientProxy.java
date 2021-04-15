package pr1Java.networking.reflection;

import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.model.exceptions.NetworkingException;
import pr1Java.model.observers.IObserver;
import pr1Java.networking.Configuration;
import pr1Java.networking.reflection.datatransfer.*;
import pr1Java.services.reflection.ReflectionServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Collection;

public class ReflectionClientProxy implements Runnable, IObserver {

    private final ReflectionServices services;
    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ReflectionClientProxy(ReflectionServices services, Socket connection) {
        this.services = services;
        this.connection = connection;
    }

    private void ensureConnection() throws NetworkingException {
        if (input != null && output != null)
            return;
        if (connection == null)
            throw new NetworkingException("lost connection to client");

        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
            Configuration.logger.trace("ensured connection {} {} {}", connection, input, output);
        } catch (IOException exception) {
            throw new NetworkingException("could not ensure connection");
        }
    }

    private void closeConnection() {
        try {
            input.close();
            output.close();
            connection.close();
            Configuration.logger.trace("closed connection");
        } catch (IOException exception) {
            Configuration.logger.error("could not close connection");
        }
    }

    @Override
    public void run() {
        Configuration.logger.traceEntry();

        try {
            ensureConnection();
            while (connected) {
                try {
                    ensureConnection();
                    Configuration.logger.trace("waiting request");
                    Request request = (Request) input.readObject();
                    Configuration.logger.trace("request received {}", request);
                    Response response = handleRequest(request);
                    if (response != null)
                        sendResponse(response);
                    Configuration.logger.trace("request handled {} and response sent {}", request, response);
//                Thread.sleep(1000);
                } catch (Exception exception) {
                    Configuration.logger.error(exception);
                }
            }
            closeConnection();
        } catch (Exception exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit();
    }

    private Response handleRequest(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        String handlerName = "handle" + request.getType();
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);

            Configuration.logger.traceExit();
            return (Response) method.invoke(this, request);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {

            Configuration.logger.traceExit();
            return new Response.Builder().setType(ResponseType.ERROR).setData("unknown request").build();
        }
    }

    private Response handleSIGN_IN(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        Response response;
        try {
            User user = DtoUtils.toUser((UserDto) request.getData());
            services.signInUser(user.getUsername(), user.getPassword(), this);
            response = new Response.Builder().setType(ResponseType.OK).setData(request.getData()).build();
        } catch (Exception exception) {
            response = new Response.Builder().setType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }

        Configuration.logger.traceExit(response);
        return response;
    }

    private Response handleSIGN_OUT(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        Response response;
        try {
            services.signOutUser(DtoUtils.toUsername((UsernameDto) request.getData()), this);
            connected = false;
            response = new Response.Builder().setType(ResponseType.OK).build();
        } catch (Exception exception) {
            response = new Response.Builder().setType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }

        Configuration.logger.traceExit(response);
        return response;
    }

    private Response handleSIGN_UP(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        Response response;
        try {
            User user = DtoUtils.toUser((UserDto) request.getData());
            services.signUpUser(user.getUsername(), user.getPassword(), this);
            response = new Response.Builder().setType(ResponseType.OK).setData(request.getData()).build();
        } catch (Exception exception) {
            response = new Response.Builder().setType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }

        Configuration.logger.traceExit(response);
        return response;
    }

    private Response handleGET_ALL_GAMES(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        Response response;
        try {
            Collection<Game> gamesCollection = services.getAllGames();
            GameCollectionDto gameCollectionDto = DtoUtils.toDto(gamesCollection);
            response = new Response.Builder().setType(ResponseType.OK).setData(gameCollectionDto).build();
        } catch (Exception exception) {
            response = new Response.Builder().setType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }

        Configuration.logger.traceExit(response);
        return response;
    }

    private Response handleSELL_SEATS(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        Response response;
        try {
            SeatsSellingDto seatsSellingDto = (SeatsSellingDto) request.getData();
            Game game = DtoUtils.toGame(seatsSellingDto.getGameDto());
            services.sellSeats(game, seatsSellingDto.getClientName(), seatsSellingDto.getSeatsCount());
            response = new Response.Builder().setType(ResponseType.OK).build();
        } catch (Exception exception) {
            response = new Response.Builder().setType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }

        Configuration.logger.traceExit(response);
        return response;
    }

    private Response handleGET_GAMES_WITH_AVAILABLE_SEATS_DESCENDING(Request request) {
        Configuration.logger.traceEntry("entering with {}", request);

        Response response;
        try {
            Collection<Game> gamesCollection = services.getGamesWithAvailableSeatsDescending();
            GameCollectionDto gameCollectionDto = DtoUtils.toDto(gamesCollection);
            response = new Response.Builder().setType(ResponseType.OK).setData(gameCollectionDto).build();
        } catch (Exception exception) {
            response = new Response.Builder().setType(ResponseType.ERROR).setData(exception.getMessage()).build();
        }

        Configuration.logger.traceExit(response);
        return response;
    }

    private void sendResponse(Response response) throws NetworkingException {
        Configuration.logger.traceEntry("entering with {}", response);

        ensureConnection();
        try {
            output.writeObject(response);
            output.flush();
            Configuration.logger.trace("sent response {}", response);
        } catch (IOException exception) {
            throw new NetworkingException("error sending response " + exception);
        }

        Configuration.logger.traceExit();
    }

    @Override
    public void seatsSold(Integer gameId, Integer seatsCount) {
        Configuration.logger.traceEntry("entering with {} {}", gameId, seatsCount);

        try {
            SeatsSoldDto seatsSoldDto = DtoUtils.toDto(gameId, seatsCount);
            Response response = new Response.Builder().setType(ResponseType.SEATS_SOLD).setData(seatsSoldDto).build();
            sendResponse(response);
        } catch (Exception exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit();
    }
}
