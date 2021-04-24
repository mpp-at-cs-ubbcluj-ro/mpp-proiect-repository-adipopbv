package pr1Java.server.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import pr1Java.persistence.GameRepository;
import pr1Java.persistence.TicketRepository;
import pr1Java.persistence.UserRepository;
import pr1Java.persistence.database.GameDbRepository;
import pr1Java.persistence.database.TicketDbRepository;
import pr1Java.persistence.database.UserDbRepository;
import pr1Java.server.Configuration;
import pr1Java.services.thrift.ThriftHandler;
import pr1Java.services.thrift.ThriftServices;

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
        ThriftServices.Iface services = new ThriftHandler(userRepository, gameRepository, ticketRepository);
        Configuration.logger.trace("created {} instance", services);

        try {
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(new TServerSocket(serverPort)).processor(new ThriftServices.Processor<>(services)));
            Configuration.logger.trace("created {} instance", server);

            server.serve();
        } catch (TTransportException e) {
            Configuration.logger.error("could not start the server", e);
        }

        Configuration.logger.traceExit();
    }
}
