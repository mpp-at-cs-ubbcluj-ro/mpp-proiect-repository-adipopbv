package pr1Java.networking.dataTransfer;

import java.io.Serializable;

public class SeatsSoldDto implements Serializable {

    private final Integer gameId;
    private final Integer seatsCount;

    public SeatsSoldDto(Integer gameId, Integer seatsCount) {
        this.gameId = gameId;
        this.seatsCount = seatsCount;
    }

    public Integer getGameId() {
        return gameId;
    }

    public Integer getSeatsCount() {
        return seatsCount;
    }

    public String toString() {
        return "SeatsSoldDto[" + getGameId() + " " + getSeatsCount() + "]";
    }
}
