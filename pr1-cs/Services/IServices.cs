using System.Collections.Generic;
using Model;
using Model.Observers;

namespace Services
{
    public interface IServices
    {
        User SignInUser(string username, string password, IObserver client);

        void SignOutUser(string username, IObserver client);

        User SignUpUser(string username, string password, IObserver client);

        IEnumerable<Game> GetAllGames();

        void SellSeats(Game game, string clientName, int seatsCount);

        IEnumerable<Game> GetGamesWithAvailableSeatsDescending();
    }
}