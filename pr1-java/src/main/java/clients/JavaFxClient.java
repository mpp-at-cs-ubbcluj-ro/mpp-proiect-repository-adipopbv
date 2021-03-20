package clients;

import clients.controllers.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JavaFxClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/logInWindow.fxml"));
        AnchorPane root = loader.load();
        LogInController controller = loader.getController();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Log In!");
        primaryStage.show();
    }
}
