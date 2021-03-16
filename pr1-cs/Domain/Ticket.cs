namespace pr1_cs.Domain
{
    public class Ticket : Entity<int>
    {
        private Game _game;
        private int _cost;

        public Game Game
        {
            get => _game;
            set => _game = value;
        }

        public int Cost
        {
            get => _cost;
            set => _cost = value;
        }

        public Ticket(int id, Game game, int cost) : base(id)
        {
            _game = game;
            _cost = cost;
        }
    }
}