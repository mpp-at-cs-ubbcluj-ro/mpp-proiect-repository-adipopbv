import domain.Game;
import domain.Ticket;
import domain.User;
import repository.GameRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.database.GameDbRepository;
import repository.database.TicketDbRepository;
import repository.database.UserDbRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find db.config | " + e);
        }

        UserRepository userRepository = new UserDbRepository(properties);
        GameRepository gameRepository = new GameDbRepository(properties);
        TicketRepository ticketRepository = new TicketDbRepository(properties);
        userRepository.add(new User(1, "adipopbv", "logged-out"));
        gameRepository.add(new Game(1, "Semifinals", "Rockets", "Celtics", 300, 200));
        ticketRepository.add(new Ticket(1, gameRepository.getOne(1), "Pop Adrian"));
        System.out.println("All users: " + userRepository);
        System.out.println("All games: " + gameRepository);
        System.out.println("All tickets: " + ticketRepository);
        userRepository.modify(1, new User(1, "adipopbv", "logged-in"));
        gameRepository.modify(1, new Game(1, "Finals", "Celtics", "Rockets", 450, 150));
        ticketRepository.modify(1, new Ticket(1, gameRepository.getOne(1), "Adrian Pop"));
        System.out.println("All users: " + userRepository);
        System.out.println("All games: " + gameRepository);
        System.out.println("All tickets: " + ticketRepository);
        userRepository.remove(1);
        gameRepository.remove(1);
//        ticketRepository.remove(1);
        System.out.println("All users: " + userRepository);
        System.out.println("All games: " + gameRepository);
        System.out.println("All tickets: " + ticketRepository);
    }
}
