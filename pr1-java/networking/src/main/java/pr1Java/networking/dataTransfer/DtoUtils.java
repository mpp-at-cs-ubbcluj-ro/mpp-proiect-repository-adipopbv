package pr1Java.networking.dataTransfer;

import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.networking.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

        Integer id = gameDto.getId();
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

    public static String toUsername(UsernameDto usernameDto) {
        Configuration.logger.traceEntry("entering with {}", usernameDto);

        String username = usernameDto.getValue();

        Configuration.logger.traceExit(username);
        return username;
    }

    public static UsernameDto toDto(String username) {
        Configuration.logger.traceEntry("entering with {}", username);

        UsernameDto usernameDto = new UsernameDto(username);

        Configuration.logger.traceExit(usernameDto);
        return usernameDto;
    }

    public static SeatsSellingDto toDto(Game game, String clientName, Integer seatsCount) {
        Configuration.logger.traceEntry("entering with {} {} {}", game, clientName, seatsCount);

        SeatsSellingDto seatsSellingDto = new SeatsSellingDto(toDto(game), clientName, seatsCount);

        Configuration.logger.traceExit(seatsSellingDto);
        return seatsSellingDto;
    }

    public static Collection<Game> toGameCollection(GameCollectionDto gameCollectionDto) {
        Configuration.logger.traceEntry("entering with {}", gameCollectionDto);

        Collection<Game> games = new ArrayList<>();
        for (GameDto gameDto : gameCollectionDto.getGames()) {
            games.add(toGame(gameDto));
        }

        Configuration.logger.traceExit(games);
        return games;
    }

    public static GameCollectionDto toDto(Collection<Game> gameCollection) {
        Configuration.logger.traceEntry("entering with {}", gameCollection);

        List<Game> gameList = new ArrayList<>(gameCollection);
        GameDto[] gameDtos = new GameDto[gameList.size()];
        for (int index = 0; index < gameList.size(); index++) {
            gameDtos[index] = toDto(gameList.get(index));
        }
        GameCollectionDto gameCollectionDto = new GameCollectionDto(gameDtos);

        Configuration.logger.traceExit(gameCollectionDto);
        return gameCollectionDto;
    }

    public static SeatsSoldDto toDto(Integer gameId, Integer seatsCount) {
        Configuration.logger.traceEntry("entering with {} {}", gameId, seatsCount);

        SeatsSoldDto seatsSoldDto = new SeatsSoldDto(gameId, seatsCount);

        Configuration.logger.traceExit(seatsSoldDto);
        return seatsSoldDto;
    }
}
