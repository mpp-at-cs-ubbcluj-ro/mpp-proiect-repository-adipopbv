using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Model;
using Model.Exceptions;
using Persistence;
using Services.Thrift.DataTransfer;

namespace Services.Thrift
{
    public class ThriftHandler : ThriftServices.IAsync
    {
        private readonly IGameRepository _gameRepository;
        private readonly List<string> _signedInUsernames;
        private readonly ITicketRepository _ticketRepository;
        private readonly IUserRepository _userRepository;

        public ThriftHandler(IUserRepository userRepository, IGameRepository gameRepository,
            ITicketRepository ticketRepository)
        {
            _userRepository = userRepository;
            _gameRepository = gameRepository;
            _ticketRepository = ticketRepository;
            _signedInUsernames = new List<string>();
        }

        public async Task<UserDto> signInUser(string username, string password, CancellationToken cancellationToken = default)
        {
            if (_signedInUsernames.Contains(username))
                throw new SignInException("user already signed in");
            var user = _userRepository.GetOne(username);
            if (user.Password != password)
                throw new SignInException("incorrect password");
            _signedInUsernames.Add(user.Username);
            return await Task.FromResult(DtoUtils.ToDto(user));
        }

        public async Task signOutUser(string username, CancellationToken cancellationToken = default)
        {
            if (!_signedInUsernames.Contains(username))
                throw new SignInException("user already signed out");
            var user = _userRepository.GetOne(username);
            _signedInUsernames.Remove(user.Username);
            await Task.CompletedTask;
        }

        public async Task<UserDto> signUpUser(string username, string password, CancellationToken cancellationToken = default)
        {
            var user = _userRepository.Add(new User(username, password));
            await signInUser(username, password);
            return await Task.FromResult(DtoUtils.ToDto(user));
        }

        public async Task<List<GameDto>> getAllGames(CancellationToken cancellationToken = default)
        {
            var gameDtos = new List<GameDto>();
            foreach (var game in _gameRepository.GetAll())
                gameDtos.Add(DtoUtils.ToDto(game));
            return await Task.FromResult(gameDtos);
        }

        public async Task sellSeats(GameDto game, string clientName, int seatsCount, CancellationToken cancellationToken = default)
        {
            if (game.AvailableSeats < seatsCount)
                throw new ParameterException("not enough seats available");
            _gameRepository.SetGameAvailableSeats(game.GameId, game.AvailableSeats - seatsCount);
            NotifySeatsSold(game.GameId, seatsCount);
            while (seatsCount != 0)
            {
                try
                {
                    _ticketRepository.Add(new Ticket(DtoUtils.ToGame(game), clientName));
                }
                catch (DuplicateException)
                {
                }

                seatsCount--;
            }
            await Task.CompletedTask;
        }

        private void NotifySeatsSold(int gameId, int seatsCount)
        {
            foreach (var client in _signedInUsernames)
            {
                // Task.Run(() => client.SeatsSold(gameId, seatsCount));
            }
        }

        public async Task<List<GameDto>> getGamesWithAvailableSeatsDescending(CancellationToken cancellationToken = default)
        {
            var gameDtos = new List<GameDto>();
            foreach (var game in _gameRepository.GetGamesByAvailableSeatsDescending(true))
                if (game.AvailableSeats > 0)
                    gameDtos.Add(DtoUtils.ToDto(game));
            return await Task.FromResult(gameDtos);
        }
    }
}