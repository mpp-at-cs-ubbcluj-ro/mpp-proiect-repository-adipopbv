package pr1Java.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.services.IObserver;
import pr1Java.services.IServices;

import java.util.Collection;

public class MainController extends Controller implements IObserver {
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
    public CheckBox switchFilterCheckBox;
    ObservableList<Game> gameObservableList = FXCollections.observableArrayList();
    Collection<Game> games;

    @Override
    public void init(IServices server, User signedInUser) {
        super.init(server, signedInUser);
        loadGameTableData();

        welcomeText.setText("Welcome " + signedInUser.getUsername() + "!");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        homeTeamColumn.setCellValueFactory(new PropertyValueFactory<>("homeTeam"));
        awayTeamColumn.setCellValueFactory(new PropertyValueFactory<>("awayTeam"));
        seatCostColumn.setCellValueFactory(new PropertyValueFactory<>("seatCost"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        availableSeatsColumn.setCellFactory(availableSeat -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item <= 0) {
                    setText("SOLD OUT");
                    setStyle("-fx-text-fill: red");
                } else if (!empty) {
                    setText(item.toString());
                    setStyle("-fx-text-fill: black");
                } else {
                    setText("");
                    setStyle("-fx-text-fill: black");
                }
            }
        });
        gamesTable.setItems(gameObservableList);
    }

    private void loadGameTableData() {
        try {
            if (switchFilterCheckBox.isSelected())
                games = server.getGamesWithAvailableSeatsDescending();
            else {
                games = server.getAllGames();
            }
            gameObservableList.setAll(games);
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    @FXML
    public void sellSeats() {
        try {
            if (gamesTable.getSelectionModel().getSelectedItem() == null)
                throw new NotFoundException("no game selected");
            server.sellSeats(gamesTable.getSelectionModel().getSelectedItem(), clientNameField.getText(), seatsCountSpinner.getValue());
            gamesTable.getSelectionModel().clearSelection();
            clientNameField.clear();
            seatsCountSpinner.getValueFactory().setValue(1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "seats sold successfully");
            alert.show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    @FXML
    private void showGamesFiltered() {
        loadGameTableData();
    }

    @FXML
    public void signOut() {
        try {
            server.signOutUser(signedInUser.getUsername(), this);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "logged out successfully");
            alert.show();
            ((Stage) welcomeText.getScene().getWindow()).close();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    @Override
    public void seatsSold(Game game) {
        for (Game searchedGame : games) {
            if (searchedGame.getId().equals(game.getId())) {
                searchedGame.setAvailableSeats(game.getAvailableSeats());
                break;
            }
        }
        gameObservableList.setAll(games);
    }
}
