using System.Collections.Generic;
using pr1_cs.Domain;

namespace pr1_cs.Repository
{
    public interface IGameRepository : IRepository<int, Game>
    {
        IEnumerable<Game> GetGamesOrderedByAvailableSeats(bool reverse);
    }
}