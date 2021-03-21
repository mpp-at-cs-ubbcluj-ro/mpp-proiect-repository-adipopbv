package userInterfacing;

import domain.exceptions.DuplicateException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Configuration;

import java.io.IOException;

public class SignUpController extends UserInterface {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button signUpButton;
    @FXML
    public Button toLogInButton;

    @FXML
    public void signUp() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/mainWindow.fxml"));
            Parent parent = loader.load();
            ((MainController) loader.getController()).init(service, service.signUpUser(usernameField.getText()));
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Basketball teams app");
            stage.show();
            ((Stage) signUpButton.getScene().getWindow()).close();
        } catch (DuplicateException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        } catch (IOException exception) {
            Configuration.logger.error(exception);
        }
    }

    @FXML
    public void toLogIn() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/logInWindow.fxml"));
            Parent parent = loader.load();
            ((LogInController) loader.getController()).init(service, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Log In");
            stage.show();
            ((Stage) toLogInButton.getScene().getWindow()).close();
        } catch (IOException exception) {
            Configuration.logger.error(exception);
        }
    }
}
