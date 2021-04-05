using System;

namespace Networking.DataTransfer
{
    [Serializable]
    public class GameCollectionDto
    {
        public GameCollectionDto(GameDto[] games)
        {
            Games = games;
        }

        public GameDto[] Games { get; }
    }
}