package domain;

public class Game extends Entity<Integer>{
    private String name;
    private String homeTeam;
    private String awayTeam;
    private Integer availableSeats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Game(Integer id, String name, String homeTeam, String awayTeam, Integer availableSeats) {
        super(id);
        this.name = name;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
