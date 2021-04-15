package pr1Java.networking.reflection.datatransfer;

import java.io.Serializable;

public class GameCollectionDto implements Serializable {
    private final GameDto[] games;

    public GameCollectionDto(GameDto[] games) {
        this.games = games;
    }

    public GameDto[] getGames() {
        return games;
    }
}
