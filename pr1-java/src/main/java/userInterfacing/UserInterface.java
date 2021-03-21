package userInterfacing;

import domain.User;
import services.Service;

public abstract class UserInterface {
    protected Service service = null;
    protected User loggedInUser = null;

    public void init(Service service, User loggedInUser) {
        this.service = service;
        this.loggedInUser = loggedInUser;
    }
}
