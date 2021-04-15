using System;

namespace Networking.Reflection.DataTransfer
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