package pr1Java.server.servers;

import pr1Java.model.exceptions.ServerException;
import pr1Java.server.Configuration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server {

    private final Integer port;
    private ServerSocket serverSocket = null;

    public Server(Integer port) {
        this.port = port;
    }

    public void start() throws ServerException {
        Configuration.logger.traceEntry();

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Configuration.logger.trace("waiting for clients on {}", serverSocket);
                Socket clientSocket = serverSocket.accept();
                Configuration.logger.trace("client connected {}", clientSocket);
                beginConversation(clientSocket);
            }
        } catch (IOException exception) {
            throw new ServerException("error trying to connect to client");
        } finally {
            Configuration.logger.traceExit();
            stop();
        }
    }

    public void stop() throws ServerException {
        Configuration.logger.traceEntry();

        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("could not close the server");
        }

        Configuration.logger.traceExit();
    }

    protected abstract void beginConversation(Socket connection);

}
