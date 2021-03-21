package userInterfacing;

import domain.User;
import services.Service;
import utils.observers.Observer;

public abstract class UserInterface implements Observer {
    protected Service service = null;
    protected User loggedInUser = null;

    public void init(Service service, User loggedInUser) {
        this.service = service;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public void update() {

    }
}
