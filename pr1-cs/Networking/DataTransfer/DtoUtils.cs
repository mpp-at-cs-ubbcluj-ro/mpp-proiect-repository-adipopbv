using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using Model;

namespace Networking.DataTransfer
{
    public class DtoUtils
    {
        public static User ToUser(UserDto userDto)
        {
            var username = userDto.Username;
            var password = userDto.Password;
            var user = new User(username, password);

            return user;
        }

        public static UserDto ToDto(User user)
        {
            var username = user.Username;
            var password = user.Password;
            var userDto = new UserDto(username, password);

            return userDto;
        }

        public static Game ToGame(GameDto gameDto)
        {
            var id = gameDto.Id;
            var name = gameDto.Name;
            var homeTeam = gameDto.HomeTeam;
            var awayTeam = gameDto.AwayTeam;
            var availableSeats = gameDto.AvailableSeats;
            var seatCost = gameDto.SeatCost;
            var game = new Game(id, name, homeTeam, awayTeam, availableSeats, seatCost);

            return game;
        }

        public static GameDto ToDto(Game game)
        {
            var id = game.Id;
            var name = game.Name;
            var homeTeam = game.HomeTeam;
            var awayTeam = game.AwayTeam;
            var availableSeats = game.AvailableSeats;
            var seatCost = game.SeatCost;
            var gameDto = new GameDto(id, name, homeTeam, awayTeam, availableSeats, seatCost);

            return gameDto;
        }

        public static string ToUsername(UsernameDto usernameDto)
        {
            var username = usernameDto.Value;

            return username;
        }

        public static UsernameDto ToDto(string username)
        {
            var usernameDto = new UsernameDto(username);

            return usernameDto;
        }

        public static SeatsSellingDto ToDto(Game game, string clientName, int seatsCount)
        {
            var seatsSellingDto = new SeatsSellingDto(ToDto(game), clientName, seatsCount);

            return seatsSellingDto;
        }

        public static Collection<Game> ToGameCollection(GameCollectionDto gameCollectionDto)
        {
            var games = new Collection<Game>();
            foreach (var gameDto in gameCollectionDto.Games)
                games.Add(ToGame(gameDto));

            return games;
        }

        public static GameCollectionDto ToDto(Collection<Game> gameCollection)
        {
            var gameDtos = new GameDto[gameCollection.Count];
            for (var index = 0; index < gameCollection.Count; index++) gameDtos[index] = ToDto(gameCollection[index]);
            var gameCollectionDto = new GameCollectionDto(gameDtos);

            return gameCollectionDto;
        }

        public static SeatsSoldDto ToDto(int gameId, int seatsCount)
        {
            var seatsSoldDto = new SeatsSoldDto(gameId, seatsCount);

            return seatsSoldDto;
        }
    }
}