import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.GameRepository;
import repository.TicketRepository;
import repository.UserRepository;
import repository.database.GameDbRepository;
import repository.database.TicketDbRepository;
import repository.database.UserDbRepository;
import services.Service;
import userInterfacing.controllers.LogInController;
import utils.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class JavaFxMain extends Application {

    public static void main(String[] args) {
        Configuration.loadProperties("app.config");
        Configuration.initLogger();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserRepository userRepository = new UserDbRepository();
        GameRepository gameRepository = new GameDbRepository();
        TicketRepository ticketRepository = new TicketDbRepository();
        Service service = new Service();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/logInWindow.fxml"));
        Parent root = loader.load();
        ((LogInController) loader.getController()).init(service);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
