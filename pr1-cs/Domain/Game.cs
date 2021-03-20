namespace pr1_cs.Domain
{
    public class Game : Entity<int>
    {
        private string _name;
        private string _homeTeam;
        private string _awayTeam;
        private int _availableSeats;
        private int _seatCost;

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

        public int SeatCost
        {
            get => _seatCost;
            set => _seatCost = value;
        }

        public Game(int id, string name, string homeTeam, string awayTeam, int availableSeats, int seatCost) : base(id)
        {
            _name = name;
            _homeTeam = homeTeam;
            _awayTeam = awayTeam;
            _availableSeats = availableSeats;
            _seatCost = seatCost;
        }

        public override string ToString()
        {
		    return "Game{" +
                "id='" + Id+ '\'' +
                ", name='" + Name + '\'' +
                ", homeTeam='" + HomeTeam + '\'' +
                ", awayTeam='" + AwayTeam + '\'' +
                ", availableSeats='" + AvailableSeats + '\'' +
                ", seatCost='" + SeatCost + '\'' +
                '}';
        }
    }
}