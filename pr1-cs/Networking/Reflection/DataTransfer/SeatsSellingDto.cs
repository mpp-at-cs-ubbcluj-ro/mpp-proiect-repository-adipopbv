using System;

namespace Networking.Reflection.DataTransfer
{
    [Serializable]
    public class SeatsSellingDto
    {
        public SeatsSellingDto(GameDto gameDto, string clientName, int seatsCount)
        {
            GameDto = gameDto;
            ClientName = clientName;
            SeatsCount = seatsCount;
        }

        public GameDto GameDto { get; }
        public string ClientName { get; }
        public int SeatsCount { get; }

        public override string ToString()
        {
            return "SeatsCountDto[" + GameDto + " " + ClientName + " " + SeatsCount + "]";
        }
    }
}