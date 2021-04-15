package pr1Java.server.reflection;

import pr1Java.networking.reflection.ReflectionClientProxy;
import pr1Java.server.Configuration;
import pr1Java.server.ConcurrentServer;
import pr1Java.services.reflection.ReflectionServices;

import java.net.Socket;

public class ReflectionConcurrentServer extends ConcurrentServer {

    private final ReflectionServices services;

    public ReflectionConcurrentServer(Integer port, ReflectionServices services) {
        super(port);
        this.services = services;
    }

    @Override
    protected Thread createClientProxyThread(Socket connection) {
        Configuration.logger.traceEntry("entering with", connection);

        ReflectionClientProxy clientProxy = new ReflectionClientProxy(services, connection);
        Thread clientProxyThread = new Thread(clientProxy);

        Configuration.logger.traceExit(clientProxyThread);
        return clientProxyThread;
    }
}
