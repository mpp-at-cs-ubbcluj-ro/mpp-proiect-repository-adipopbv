package domain;

public class Game extends Entity<Integer>{
    private String homeTeam;
    private String awayTeam;
    private Integer availableSeats;

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Game(String homeTeam, String awayTeam, Integer availableSeats) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.availableSeats = availableSeats;
    }
}
