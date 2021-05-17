package pr1Java.client;

import org.springframework.web.client.RestTemplate;
import pr1Java.model.Game;

import java.util.concurrent.Callable;

public class RestClientStarter {
    private final String url = "http://localhost:8080/basketball-games/games";
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        RestClientStarter client = new RestClientStarter();
        Game game1 = new Game(
                4,
                "Best game there is",
                "Team Good",
                "Team Bad",
                500,
                200
        );
        Game game2 = new Game(
                4,
                "Worst game there is",
                "Team Vai de Capul Ei",
                "Team Oh Man",
                500,
                1
        );
        try {
//            show(() -> {
//                Iterable<Game> res = client.getAll();
//                for (Game game : res) {
//                    System.out.println(game);
//                }
//            });
//            show(() -> System.out.println(client.getOne(4)));
            show(() -> System.out.println(client.create(game1)));
            show(() -> System.out.println(client.getOne(4)));
            show(() -> client.update(4, game2));
            show(() -> System.out.println(client.getOne(4)));
            show(() -> client.delete(4));
//            show(() -> {
//                Iterable<Game> res = client.getAll();
//                for (Game game : res) {
//                    System.out.println(game);
//                }
//            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

    public Iterable<Game> getAll() {
        return execute(() -> restTemplate.getForObject(url, Iterable.class));
    }

    public Game getOne(int id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", url, id), Game.class));
    }

    public Game create(Game game) {
        return execute(() -> restTemplate.postForObject(url, game, Game.class));
    }

    public void update(int id, Game game) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", url, id), game);
            return null;
        });
    }

    public void delete(int id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", url, id));
            return null;
        });
    }
}
