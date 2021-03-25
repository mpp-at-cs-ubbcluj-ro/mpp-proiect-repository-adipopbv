package pr1Java.server.servers;

import pr1Java.networking.rpcProtocol.ClientRpcProxy;
import pr1Java.server.Configuration;
import pr1Java.services.IServices;

import java.net.Socket;

public class RpcConcurrentServer extends ConcurrentServer {

    private final IServices services;

    public RpcConcurrentServer(Integer port, IServices services) {
        super(port);
        this.services = services;
    }

    @Override
    protected Thread createClientProxyThread(Socket connection) {
        Configuration.logger.traceEntry("entering with", connection);

        ClientRpcProxy clientProxy = new ClientRpcProxy(services, connection);
        Thread clientProxyThread = new Thread(clientProxy);

        Configuration.logger.traceExit(clientProxyThread);
        return clientProxyThread;
    }
}
