package pr1Java.networking.dataTransfer;

import java.io.Serializable;

public class SeatsSellingDto implements Serializable {
    private final GameDto gameDto;
    private final String clientName;
    private final Integer seatsCount;

    public SeatsSellingDto(GameDto gameDto, String clientName, Integer seatsCount) {
        this.gameDto = gameDto;
        this.clientName = clientName;
        this.seatsCount = seatsCount;
    }

    public GameDto getGameDto() {
        return gameDto;
    }

    public String getClientName() {
        return clientName;
    }

    public Integer getSeatsCount() {
        return seatsCount;
    }

    @Override
    public String toString() {
        return "SeatsCountDto[" + getGameDto() + " " + getClientName() + " " + getSeatsCount() + "]";
    }
}
