package pr1Java.client.clients;

import pr1Java.client.Configuration;
import pr1Java.model.User;
import pr1Java.services.IServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class Client extends UnicastRemoteObject implements Serializable {
    protected IServices services = null;
    protected User signedInUser = null;

    public Client() throws RemoteException {
    }

    public void init(IServices services, User signedInUser) {
        Configuration.logger.traceEntry("entering init with {} and {}", services, signedInUser);

        this.services = services;
        this.signedInUser = signedInUser;

        Configuration.logger.traceExit();
    }
}
