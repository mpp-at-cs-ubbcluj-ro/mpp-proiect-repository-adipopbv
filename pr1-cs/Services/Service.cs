using System.Collections.Generic;
using pr1_cs.Domain;
using pr1_cs.Domain.Exceptions;
using pr1_cs.Repository;

namespace pr1_cs.Services
{
    public class Service
    {
        private readonly IGameRepository _gameRepository;
        private readonly ITicketRepository _ticketRepository;
        private readonly IUserRepository _userRepository;

        public Service(IUserRepository userRepository, IGameRepository gameRepository,
            ITicketRepository ticketRepository)
        {
            _userRepository = userRepository;
            _gameRepository = gameRepository;
            _ticketRepository = ticketRepository;
        }

        public User LogInUser(string username)
        {
            var user = _userRepository.GetOne(username);
            if (user.Status == "logged-in")
                throw new LogInException("user already logged in");
            _userRepository.SetUserStatus(user.Id, "logged-in");
            return user;
        }

        public User LogOutUser(string username)
        {
            var user = _userRepository.GetOne(username);
            if (user.Status == "logged-out")
                throw new LogInException("user already logged out");
            _userRepository.SetUserStatus(user.Id, "logged-out");
            return user;
        }

        public User SignUpUser(string username)
        {
            var user = new User(username, "logged-in");
            user = _userRepository.Add(user);
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