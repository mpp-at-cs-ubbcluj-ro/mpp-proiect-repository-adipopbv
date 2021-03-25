package pr1Java.services;

import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.model.observers.IObserver;

import java.util.Collection;

public interface IServices {
    User signInUser(String username, String password, IObserver client) throws Exception;

    void signOutUser(String username, IObserver client) throws Exception;

    User signUpUser(String username, String password, IObserver client) throws Exception;

    Collection<Game> getAllGames() throws Exception;

    void sellSeats(Game game, String clientName, Integer seatsCount) throws Exception;

    Collection<Game> getGamesWithAvailableSeatsDescending() throws Exception;
}
