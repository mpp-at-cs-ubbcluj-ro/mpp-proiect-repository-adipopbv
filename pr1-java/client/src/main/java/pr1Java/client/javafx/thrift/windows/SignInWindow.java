package pr1Java.client.javafx.thrift.windows;

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
import pr1Java.services.thrift.datatransfer.ConnectionInfo;
import pr1Java.services.thrift.datatransfer.DtoUtils;

public class SignInWindow extends Window {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    @FXML
    public void signIn() {
        Configuration.logger.traceEntry();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/thrift/mainWindow.fxml"));
            Parent parent = loader.load();
            MainWindow controller = loader.getController();

            User user = DtoUtils.toUser(services.signInUser(usernameField.getText(), passwordField.getText(), connectionInfo));
            Configuration.logger.trace("signed in {} ", user);

            controller.init(connection, services, user);
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
            loader.setLocation(getClass().getResource("/views/thrift/signUpWindow.fxml"));
            Parent parent = loader.load();
            ((SignUpWindow) loader.getController()).init(connection, services, null);
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
