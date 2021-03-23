package userInterfacing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.database.GameDbRepository;
import repository.database.TicketDbRepository;
import repository.database.UserDbRepository;
import services.Service;

public class JavaFxClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/logInWindow.fxml"));
        Parent root = loader.load();
        ((LogInController) loader.getController()).init(new Service(new UserDbRepository(), new GameDbRepository(), new TicketDbRepository()), null);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log In");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
