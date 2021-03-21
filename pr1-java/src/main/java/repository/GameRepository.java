package repository;

import domain.Game;

public interface GameRepository extends Repository<Integer, Game> {

    /**
     * Gets all games ordered by available seats
     *
     * @param reverse should the games be ordered in reverse?
     * @return a list of games
     */
    Iterable<Game> getGamesOrderedByAvailableSeats(Boolean reverse);
}
