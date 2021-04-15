package pr1Java.networking.reflection.datatransfer;

import java.io.Serializable;

public class GameDto implements Serializable {
    private final Integer id;
    private final String name;
    private final String homeTeam;
    private final String awayTeam;
    private final Integer availableSeats;
    private final Integer seatCost;

    public GameDto(Integer id, String name, String homeTeam, String awayTeam, Integer availableSeats, Integer seatCost) {
        this.id = id;
        this.name = name;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.availableSeats = availableSeats;
        this.seatCost = seatCost;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public Integer getSeatCost() {
        return seatCost;
    }

    @Override
    public String toString() {
        return "GameDto[" + getId() + " " + getName() + " " + getHomeTeam() + " " + getAwayTeam() + " " + getAvailableSeats().toString() + " " + getSeatCost() + "]";
    }
}
