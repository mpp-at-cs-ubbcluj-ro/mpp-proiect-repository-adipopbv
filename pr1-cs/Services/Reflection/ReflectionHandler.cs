using System.Collections.Generic;
using System.Threading.Tasks;
using Model;
using Model.Exceptions;
using Model.Observers;
using Persistence;

namespace Services.Reflection
{
    public class ReflectionHandler : IReflectionServices
    {
        private readonly IGameRepository _gameRepository;
        private readonly IDictionary<string, IObserver> _signedInClients;
        private readonly ITicketRepository _ticketRepository;
        private readonly IUserRepository _userRepository;

        public ReflectionHandler(IUserRepository userRepository, IGameRepository gameRepository,
            ITicketRepository ticketRepository)
        {
            _userRepository = userRepository;
            _gameRepository = gameRepository;
            _ticketRepository = ticketRepository;
            _signedInClients = new Dictionary<string, IObserver>();
        }

        public User SignInUser(string username, string password, IObserver client)
        {
            if (_signedInClients.ContainsKey(username))
                throw new SignInException("user already signed in");
            var user = _userRepository.GetOne(username);
            if (user.Password != password)
                throw new SignInException("incorrect password");
            _signedInClients.Add(user.Username, client);
            return user;
        }

        public void SignOutUser(string username, IObserver client)
        {
            if (!_signedInClients.ContainsKey(username))
                throw new SignInException("user already signed out");
            var user = _userRepository.GetOne(username);
            _signedInClients.Remove(user.Username);
        }

        public User SignUpUser(string username, string password, IObserver client)
        {
            var user = _userRepository.Add(new User(username, password));
            SignInUser(username, password, client);
            return user;
        }

        public IEnumerable<Game> GetAllGames()
        {
            return _gameRepository.GetAll();
        }

        public void SellSeats(Game game, string clientName, int seatsCount)
        {
            if (game.AvailableSeats < seatsCount)
                throw new ParameterException("not enough seats available");
            _gameRepository.SetGameAvailableSeats(game.Id, game.AvailableSeats - seatsCount);
            NotifySeatsSold(game.Id, seatsCount);
            while (seatsCount != 0)
            {
                try
                {
                    _ticketRepository.Add(new Ticket(game, clientName));
                }
                catch (DuplicateException)
                {
                }

                seatsCount--;
            }
        }

        private void NotifySeatsSold(int gameId, int seatsCount)
        {
            foreach (var client in _signedInClients.Values)
            {
                Task.Run(() => client.SeatsSold(gameId, seatsCount));
            }
        }

        public IEnumerable<Game> GetGamesWithAvailableSeatsDescending()
        {
            var games = new List<Game>();
            foreach (var game in _gameRepository.GetGamesByAvailableSeatsDescending(true))
                if (game.AvailableSeats > 0)
                    games.Add(game);
            return games;
        }
    }
}