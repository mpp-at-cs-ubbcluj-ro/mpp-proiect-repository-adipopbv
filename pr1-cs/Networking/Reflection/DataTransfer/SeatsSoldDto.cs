using System;

namespace Networking.Reflection.DataTransfer
{
    [Serializable]
    public class SeatsSoldDto
    {
        public SeatsSoldDto(int gameId, int seatsCount)
        {
            GameId = gameId;
            SeatsCount = seatsCount;
        }

        public int GameId { get; }
        public int SeatsCount { get; }

        public override string ToString()
        {
            return "SeatsSoldDto[" + GameId + " " + SeatsCount + "]";
        }
    }
}