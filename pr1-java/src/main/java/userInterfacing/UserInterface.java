package userInterfacing;

import services.Service;

public abstract class UserInterface {
    protected Service service = null;

    public void init(Service service) {
        this.service = service;
    }
}
