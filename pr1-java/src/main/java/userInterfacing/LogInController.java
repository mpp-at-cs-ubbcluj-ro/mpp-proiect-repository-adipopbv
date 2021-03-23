package userInterfacing;

import domain.User;
import domain.exceptions.LogInException;
import domain.exceptions.NotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.Service;
import utils.Configuration;

import java.io.IOException;

public class LogInController extends UserInterface {
    @FXML
    public Button logInButton;
    @FXML
    public TextField userIdField;
    @FXML
    public Button toSignUpButton;

    @FXML
    public void logIn() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/mainWindow.fxml"));
            Parent parent = loader.load();
            ((MainController) loader.getController()).init(service, service.logInUser(userIdField.getText()));
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Basketball teams app");
            stage.show();
            ((Stage) logInButton.getScene().getWindow()).close();
        } catch (LogInException | NotFoundException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        } catch (IOException exception) {
            Configuration.logger.error(exception);
        }
    }

    @FXML
    public void toSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/signUpWindow.fxml"));
            Parent parent = loader.load();
            ((SignUpController) loader.getController()).init(service, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Sign Up");
            stage.show();
            ((Stage) toSignUpButton.getScene().getWindow()).close();
        } catch (IOException exception) {
            Configuration.logger.error(exception);
        }
    }

    @Override
    public void init(Service service, User loggedInUser) {
        super.init(service, loggedInUser);
    }
}
