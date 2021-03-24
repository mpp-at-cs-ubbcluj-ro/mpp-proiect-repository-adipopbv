package pr1Java.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pr1Java.client.Configuration;
import pr1Java.model.exceptions.DuplicateException;

import java.io.IOException;

public class SignUpController extends Controller {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    @FXML
    public void signUp() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/mainWindow.fxml"));
            Parent parent = loader.load();
            MainController controller = loader.getController();
            controller.init(server, server.signUpUser(usernameField.getText(), passwordField.getText(), controller));
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Basketball teams app");
            stage.setOnCloseRequest(event -> {
                controller.signOut();
                System.exit(0);
            });
            stage.show();
            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    @FXML
    public void toSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/signInWindow.fxml"));
            Parent parent = loader.load();
            ((SignInController) loader.getController()).init(server, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Log In");
            stage.show();
            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (Exception exception) {
            Configuration.logger.error(exception);
        }
    }
}
