namespace Model
{
    public class Game : Entity<int>
    {
        public Game(int id, string name, string homeTeam, string awayTeam, int availableSeats, int seatCost) : base(id)
        {
            Name = name;
            HomeTeam = homeTeam;
            AwayTeam = awayTeam;
            AvailableSeats = availableSeats;
            SeatCost = seatCost;
        }

        public string Name { get; set; }

        public string HomeTeam { get; set; }

        public string AwayTeam { get; set; }

        public int AvailableSeats { get; set; }

        public int SeatCost { get; set; }

        public override string ToString()
        {
            return "Game{" +
                   "id='" + Id + '\'' +
                   ", name='" + Name + '\'' +
                   ", homeTeam='" + HomeTeam + '\'' +
                   ", awayTeam='" + AwayTeam + '\'' +
                   ", availableSeats='" + AvailableSeats + '\'' +
                   ", seatCost='" + SeatCost + '\'' +
                   '}';
        }
    }
}