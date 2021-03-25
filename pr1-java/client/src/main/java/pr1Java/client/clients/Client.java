package pr1Java.client.clients;

import pr1Java.client.Configuration;
import pr1Java.model.User;
import pr1Java.services.IServices;

public abstract class Client {
    protected IServices services = null;
    protected User signedInUser = null;

    public void init(IServices services, User signedInUser) {
        Configuration.logger.traceEntry("entering init with {} and {}", services, signedInUser);

        this.services = services;
        this.signedInUser = signedInUser;

        Configuration.logger.traceExit();
    }
}
