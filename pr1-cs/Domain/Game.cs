namespace pr1_cs.Domain
{
    public class Game : Entity<int>
    {
        private string _name;
        private string _homeTeam;
        private string _awayTeam;
        private int _availableSeats;

        public string Name
        {
            get => _name;
            set => _name = value;
        }

        public string HomeTeam
        {
            get => _homeTeam;
            set => _homeTeam = value;
        }

        public string AwayTeam
        {
            get => _awayTeam;
            set => _awayTeam = value;
        }

        public int AvailableSeats
        {
            get => _availableSeats;
            set => _availableSeats = value;
        }

        public Game(int id, string name, string homeTeam, string awayTeam, int availableSeats) : base(id)
        {
            _name = name;
            _homeTeam = homeTeam;
            _awayTeam = awayTeam;
            _availableSeats = availableSeats;
        }
    }
}