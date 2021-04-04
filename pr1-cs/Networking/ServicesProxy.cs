using System.Collections.Generic;
using Model;
using Model.Observers;
using Networking.Messaging;
using Services;

namespace Networking
{
    public class ServicesProxy : IServices
    {
        private readonly string _host;
        private readonly int _port;
        private Queue<Response> _responses;

        public ServicesProxy(string host, int port)
        {
            _host = host;
            _port = port;
            _responses = new Queue<Response>();
        }

        public User SignInUser(string username, string password, IObserver client)
        {
            throw new System.NotImplementedException();
        }

        public void SignOutUser(string username, IObserver client)
        {
            throw new System.NotImplementedException();
        }

        public User SignUpUser(string username, string password, IObserver client)
        {
            throw new System.NotImplementedException();
        }

        public IEnumerable<Game> GetAllGames()
        {
            throw new System.NotImplementedException();
        }

        public void SellSeats(Game game, string clientName, int seatsCount)
        {
            throw new System.NotImplementedException();
        }

        public IEnumerable<Game> GetGamesWithAvailableSeatsDescending()
        {
            throw new System.NotImplementedException();
        }
    }
}