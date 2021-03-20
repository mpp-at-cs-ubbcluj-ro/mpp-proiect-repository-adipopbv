namespace pr1_cs.Domain
{
    public class Ticket : Entity<int>
    {
        private Game _forGame;
        private string _clientName;

        public Game ForGame
        {
            get => _forGame;
            set => _forGame = value;
        }

        public string ClientName
        {
            get => _clientName;
            set => _clientName = value;
        }

        public Ticket(int id, Game forGame, string clientName) : base(id)
        {
            _forGame = forGame;
            _clientName = clientName;
        }

        public override string ToString()
        {
		    return "Ticket{" +
                "id='" + Id+ '\'' +
                ", forGame='" + ForGame.Name + '\'' +
                ", clientName='" + ClientName + '\'' +
                '}';
        }
    }
}