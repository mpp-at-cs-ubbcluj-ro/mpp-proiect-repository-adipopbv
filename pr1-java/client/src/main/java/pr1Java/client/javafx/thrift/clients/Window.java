package pr1Java.client.javafx.thrift.clients;

import org.apache.thrift.transport.TTransport;
import pr1Java.client.Configuration;
import pr1Java.model.User;
import pr1Java.services.thrift.ThriftServices;

public abstract class Window {
    protected TTransport connection;
    protected ThriftServices.Client services = null;
    protected User signedInUser = null;

    public void init(TTransport connection, ThriftServices.Client services, User signedInUser) {
        Configuration.logger.traceEntry("entering init with {} and {}", services, signedInUser);

        this.connection = connection;
        this.services = services;
        this.signedInUser = signedInUser;

        Configuration.logger.traceExit();
    }
}
