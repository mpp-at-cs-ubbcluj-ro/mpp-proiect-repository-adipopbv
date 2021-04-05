using System;

namespace Networking.DataTransfer
{
    [Serializable]
    public class GameDto
    {
        public GameDto(int id, string name, string homeTeam, string awayTeam, int availableSeats, int seatCost)
        {
            Id = id;
            Name = name;
            HomeTeam = homeTeam;
            AwayTeam = awayTeam;
            AvailableSeats = availableSeats;
            SeatCost = seatCost;
        }

        public int Id { get; }
        public string Name { get; }
        public string HomeTeam { get; }
        public string AwayTeam { get; }
        public int AvailableSeats { get; }
        public int SeatCost { get; }

        public override string ToString()
        {
            return "GameDto[" + Id + " " + Name + " " + HomeTeam + " " + AwayTeam + " " + AvailableSeats + " " +
                   SeatCost + "]";
        }
    }
}