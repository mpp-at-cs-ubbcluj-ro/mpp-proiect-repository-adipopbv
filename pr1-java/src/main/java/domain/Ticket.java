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

    public Ticket(Integer id, Game game, Integer cost) {
        super(id);
        this.game = game;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + getId() + '\'' +
                ", game=" + game.getName() +
                ", cost=" + cost +
                '}';
    }
}
