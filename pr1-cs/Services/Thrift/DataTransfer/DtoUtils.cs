using Model;

namespace Services.Thrift.DataTransfer
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
            var userDto = new UserDto {Username = username, Password = password};

            return userDto;
        }

        public static Game ToGame(GameDto gameDto)
        {
            var id = gameDto.GameId;
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
            var gameDto = new GameDto
            {
                GameId = id, Name = name, HomeTeam = homeTeam, AwayTeam = awayTeam, AvailableSeats = availableSeats,
                SeatCost = seatCost
            };

            return gameDto;
        }
    }
}