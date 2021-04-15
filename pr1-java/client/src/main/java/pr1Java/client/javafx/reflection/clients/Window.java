package pr1Java.client.javafx.reflection.clients;

import pr1Java.client.Configuration;
import pr1Java.model.User;
import pr1Java.services.reflection.ReflectionServices;

public abstract class Window {
    protected ReflectionServices services = null;
    protected User signedInUser = null;

    public void init(ReflectionServices services, User signedInUser) {
        Configuration.logger.traceEntry("entering init with {} and {}", services, signedInUser);

        this.services = services;
        this.signedInUser = signedInUser;

        Configuration.logger.traceExit();
    }
}
