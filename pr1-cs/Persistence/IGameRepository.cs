using System.Collections.Generic;
using Model;

namespace Persistence
{
    public interface IGameRepository : IRepository<int, Game>
    {
        IEnumerable<Game> GetGamesByAvailableSeatsDescending(bool reverse);
        
        Game SetGameAvailableSeats(int id, int availableSeats);
    }
}