package userInterfacing;

import domain.User;
import services.Service;

public class MainController extends UserInterface {
    @Override
    public void init(Service service, User loggedInUser) {
        super.init(service, loggedInUser);
    }
}
