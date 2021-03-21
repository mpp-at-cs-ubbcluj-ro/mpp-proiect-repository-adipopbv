package userInterfacing.controllers;

import userInterfacing.UserInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController extends UserInterface {
    @FXML
    public Button logInAcceptButton;
    @FXML
    public TextField logInUserIdField;
    @FXML
    public Button logInToSignUpButton;

    public void logIn() {
        // TODO(adipopbv) getting login info and sending it to service

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/views/mainWindow.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Basketball teams app");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toSignUp() {

    }
}
