package pr1Java.server.reflection;

import pr1Java.model.exceptions.ServerException;
import pr1Java.persistence.GameRepository;
import pr1Java.persistence.TicketRepository;
import pr1Java.persistence.UserRepository;
import pr1Java.persistence.database.GameDbRepository;
import pr1Java.persistence.database.TicketDbRepository;
import pr1Java.persistence.database.UserDbRepository;
import pr1Java.server.Configuration;
import pr1Java.server.Server;
import pr1Java.server.reflection.ReflectionConcurrentServer;
import pr1Java.services.reflection.ReflectionServices;
import pr1Java.services.reflection.ReflectionHandler;

public class ServerStarter {

    private static final int defaultPort = 55555;

    public static void main(String[] args) {
        Configuration.logger.traceEntry();

        Configuration.loadProperties("./server/server.config");

        Integer serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(Configuration.properties.getProperty("server.port"));
        } catch (NumberFormatException exception) {
            Configuration.logger.error("wrong port number {}", exception.getMessage());
            Configuration.logger.warn("using default port {}", defaultPort);
        }
        Configuration.logger.info("starting server on port {}", serverPort);

        UserRepository userRepository = new UserDbRepository();
        GameRepository gameRepository = new GameDbRepository();
        TicketRepository ticketRepository = new TicketDbRepository();
        ReflectionServices services = new ReflectionHandler(userRepository, gameRepository, ticketRepository);
        Configuration.logger.trace("created {} instance", services);

        Server server = new ReflectionConcurrentServer(serverPort, services);
        Configuration.logger.trace("created {} instance", server);
        try {
            server.start();
        } catch (ServerException exception) {
            Configuration.logger.error("could not start the server", exception);
        } finally {
            try {
                server.stop();
            } catch (ServerException exception) {
                Configuration.logger.error("could not stop the server", exception);
            }
        }

        Configuration.logger.traceExit();
    }
}
