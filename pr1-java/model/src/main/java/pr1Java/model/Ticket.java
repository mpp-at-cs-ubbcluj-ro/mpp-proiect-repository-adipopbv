package pr1Java.model;

public class Ticket extends Entity<Integer> {
    private Game forGame;
    private String clientName;

    public Ticket() {

    }

    public Ticket(Game forGame, String clientName) {
        this.forGame = forGame;
        this.clientName = clientName;
    }

    public Ticket(Integer id, Game forGame, String clientName) {
        super(id);
        this.forGame = forGame;
        this.clientName = clientName;
    }

    public Game getForGame() {
        return forGame;
    }

    public void setForGame(Game forGame) {
        this.forGame = forGame;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String cost) {
        this.clientName = cost;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + getId() + '\'' +
                ", forGame=" + forGame.getName() +
                ", clientName=" + clientName +
                '}';
    }
}
