import userInterfacing.JavaFxClient;
import utils.Configuration;

public class Main {

    public static void main(String[] args) {
        Configuration.loadProperties("app.config");
        Configuration.initLogger();

        JavaFxClient.main(args);
    }
}
