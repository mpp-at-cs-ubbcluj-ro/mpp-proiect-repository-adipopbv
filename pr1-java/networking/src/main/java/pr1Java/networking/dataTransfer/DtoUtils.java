package pr1Java.networking.dataTransfer;

import pr1Java.model.Game;
import pr1Java.model.User;

import java.util.ArrayList;
import java.util.Collection;

public class DtoUtils {

    public static User toUser(UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        return new User(username, password);
    }

    public static UserDto toDto(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        return new UserDto(username, password);
    }

    public static Game toGame(GameDto gameDto) {
        Integer id = gameDto.getId();
        String name = gameDto.getName();
        String homeTeam = gameDto.getHomeTeam();
        String awayTeam = gameDto.getAwayTeam();
        Integer availableSeats = gameDto.getAvailableSeats();
        Integer seatCost = gameDto.getSeatCost();
        return new Game(id, name, homeTeam, awayTeam, availableSeats, seatCost);
    }

    public static GameDto toDto(Game game) {
        Integer id = game.getId();
        String name = game.getName();
        String homeTeam = game.getHomeTeam();
        String awayTeam = game.getAwayTeam();
        Integer availableSeats = game.getAvailableSeats();
        Integer seatCost = game.getSeatCost();
        return new GameDto(id, name, homeTeam, awayTeam, availableSeats, seatCost);
    }

    public static String toUsername(UsernameDto usernameDto) {
        return usernameDto.getValue();
    }

    public static UsernameDto toDto(String username) {
        return new UsernameDto(username);
    }

    public static Integer toSeatsCount(SeatsCountDto seatsCountDto) {
        return seatsCountDto.getValue();
    }

    public static SeatsCountDto toDto(Integer seatsCount) {
        return new SeatsCountDto(seatsCount);
    }

    public static Collection<Game> toGameCollection(GameCollectionDto gameCollectionDto) {
        Collection<Game> games = new ArrayList<>();
        for (GameDto gameDto : gameCollectionDto.getGames()) {
            games.add(toGame(gameDto));
        }
        return games;
    }
}
