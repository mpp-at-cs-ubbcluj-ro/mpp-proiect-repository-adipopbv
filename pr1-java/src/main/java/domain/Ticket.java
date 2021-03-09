package domain;

public class Ticket extends Entity<Integer>{
    private Game game;
    private Integer cost;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Ticket(Game game, Integer cost) {
        this.game = game;
        this.cost = cost;
    }
}
