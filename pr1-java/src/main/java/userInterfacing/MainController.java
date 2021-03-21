package userInterfacing;

import domain.Game;
import domain.User;
import domain.exceptions.LogInException;
import domain.exceptions.NotFoundException;
import domain.exceptions.ParameterException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.Service;

public class MainController extends UserInterface {
    @FXML
    public Text welcomeText;
    @FXML
    public TableView<Game> gamesTable;
    @FXML
    public TableColumn<Game, String> nameColumn;
    @FXML
    public TableColumn<Game, String> homeTeamColumn;
    @FXML
    public TableColumn<Game, String> awayTeamColumn;
    @FXML
    public TableColumn<Game, Integer> seatCostColumn;
    @FXML
    public TableColumn<Game, Integer> availableSeatsColumn;
    @FXML
    public TextField clientNameField;
    @FXML
    public Spinner<Integer> seatsCountSpinner;
    @FXML
    public Button sellSeatsButton;
    @FXML
    public CheckBox switchFilterCheckBox;
    @FXML
    public Button logOutButton;
    ObservableList<Game> games = FXCollections.observableArrayList();

    @Override
    public void init(Service service, User loggedInUser) {
        super.init(service, loggedInUser);
        update();

        welcomeText.setText("Welcome " + loggedInUser.getUsername() + "!");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        homeTeamColumn.setCellValueFactory(new PropertyValueFactory<>("homeTeam"));
        awayTeamColumn.setCellValueFactory(new PropertyValueFactory<>("awayTeam"));
        seatCostColumn.setCellValueFactory(new PropertyValueFactory<>("seatCost"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        gamesTable.setItems(games);
    }

    @FXML
    public void logOut() {
        try {
            service.logOutUser(loggedInUser.getUsername());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "logged out successfully");
            alert.show();
            ((Stage) logOutButton.getScene().getWindow()).close();
        } catch (LogInException | NotFoundException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    @FXML
    public void sellSeats() {
        try {
            service.sellSeats(gamesTable.getSelectionModel().getSelectedItem(), clientNameField.getText(), seatsCountSpinner.getValue());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "seats sold successfully");
            alert.show();
        } catch (ParameterException | NotFoundException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
        update();
    }

    @Override
    public void update() {
        if (switchFilterCheckBox.isSelected())
            games.setAll(service.getGamesWithAvailableSeatsDescending());
        else
            games.setAll(service.getAllGames());
    }
}
