package repository;

import domain.Game;
import domain.exceptions.NotFoundException;

public interface GameRepository extends Repository<Integer, Game> {

    /**
     * Gets all games ordered by available seats
     *
     * @param reverse should the games be ordered in reverse?
     * @return a list of games
     */
    Iterable<Game> getGamesByAvailableSeatsDescending(Boolean reverse);

    /**
     * Sets a game's available seats
     *
     * @param id             the id of the game
     * @param availableSeats the new game available seats
     * @return the updated game
     */
    Game setGameAvailableSeats(Integer id, Integer availableSeats) throws NotFoundException;
}
