package pr1Java.client.controllers;

import pr1Java.model.User;
import pr1Java.services.IServices;

public abstract class Controller {
    protected IServices server = null;
    protected User signedInUser = null;

    public void init(IServices server, User signedInUser) {
        this.server = server;
        this.signedInUser = signedInUser;
    }
}
