package pr1Java.services.thrift.datatransfer;

import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.services.Configuration;

public class DtoUtils {

    public static User toUser(UserDto userDto) {
        Configuration.logger.traceEntry("entering with {}", userDto);

        String username = userDto.getUsername();
        String password = userDto.getPassword();
        User user = new User(username, password);

        Configuration.logger.traceExit(user);
        return user;
    }

    public static UserDto toDto(User user) {
        Configuration.logger.traceEntry("entering with {}", user);

        String username = user.getUsername();
        String password = user.getPassword();
        UserDto userDto = new UserDto(username, password);

        Configuration.logger.traceExit(userDto);
        return userDto;
    }

    public static Game toGame(GameDto gameDto) {
        Configuration.logger.traceEntry("entering with {}", gameDto);

        Integer id = gameDto.getGameId();
        String name = gameDto.getName();
        String homeTeam = gameDto.getHomeTeam();
        String awayTeam = gameDto.getAwayTeam();
        Integer availableSeats = gameDto.getAvailableSeats();
        Integer seatCost = gameDto.getSeatCost();
        Game game = new Game(id, name, homeTeam, awayTeam, availableSeats, seatCost);

        Configuration.logger.traceExit(game);
        return game;
    }

    public static GameDto toDto(Game game) {
        Configuration.logger.traceEntry("entering with {}", game);

        Integer id = game.getId();
        String name = game.getName();
        String homeTeam = game.getHomeTeam();
        String awayTeam = game.getAwayTeam();
        Integer availableSeats = game.getAvailableSeats();
        Integer seatCost = game.getSeatCost();
        GameDto gameDto = new GameDto(id, name, homeTeam, awayTeam, availableSeats, seatCost);

        Configuration.logger.traceExit(gameDto);
        return gameDto;
    }
}
