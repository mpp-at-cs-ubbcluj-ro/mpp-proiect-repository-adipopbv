package pr1Java.server;

import java.net.Socket;

public abstract class ConcurrentServer extends Server {

    public ConcurrentServer(Integer port) {
        super(port);
    }

    protected void beginConversation(Socket connection) {
        Configuration.logger.traceEntry("entering with {}", connection);

        Thread clientProxyThread = createClientProxyThread(connection);
        clientProxyThread.start();

        Configuration.logger.traceExit();
    }

    protected abstract Thread createClientProxyThread(Socket connection);
}
