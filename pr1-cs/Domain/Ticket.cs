namespace pr1_cs.Domain
{
    public class Ticket : Entity<int>
    {
        public Ticket(Game forGame, string clientName)
        {
            ForGame = forGame;
            ClientName = clientName;
        }

        public Ticket(int id, Game forGame, string clientName) : base(id)
        {
            ForGame = forGame;
            ClientName = clientName;
        }

        public Game ForGame { get; set; }

        public string ClientName { get; set; }

        public override string ToString()
        {
            return "Ticket{" +
                   "id='" + Id + '\'' +
                   ", forGame='" + ForGame.Name + '\'' +
                   ", clientName='" + ClientName + '\'' +
                   '}';
        }
    }
}