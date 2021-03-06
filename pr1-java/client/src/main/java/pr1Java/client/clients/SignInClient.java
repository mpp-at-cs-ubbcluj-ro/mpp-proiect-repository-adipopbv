package pr1Java.client.clients;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pr1Java.client.Configuration;
import pr1Java.model.User;

import java.rmi.RemoteException;

public class SignInClient extends Client {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    public SignInClient() throws RemoteException {
    }

    @FXML
    public void signIn() {
        Configuration.logger.traceEntry();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/mainWindow.fxml"));
            Parent parent = loader.load();
            MainClient controller = loader.getController();

            User user = services.signInUser(usernameField.getText(), passwordField.getText(), controller);
            Configuration.logger.trace("signed in {} ", user);

            controller.init(services, user);
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
            Configuration.logger.catching(exception);

            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }

        Configuration.logger.traceExit();
    }

    @FXML
    public void toSignUp() {
        Configuration.logger.traceEntry();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/signUpWindow.fxml"));
            Parent parent = loader.load();
            ((SignUpClient) loader.getController()).init(services, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Sign Up");
            stage.show();
            ((Stage) usernameField.getScene().getWindow()).close();
        } catch (Exception exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit();
    }
}
