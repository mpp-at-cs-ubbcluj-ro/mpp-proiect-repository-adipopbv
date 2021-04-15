package pr1Java.client.javafx.reflection.clients;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pr1Java.client.Configuration;
import pr1Java.model.Game;
import pr1Java.model.User;
import pr1Java.model.exceptions.NotFoundException;
import pr1Java.model.observers.IObserver;
import pr1Java.services.reflection.ReflectionServices;

import java.util.Collection;

public class MainWindow extends Window implements IObserver {
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
    public void init(ReflectionServices services, User signedInUser) {
        Configuration.logger.traceEntry("entering init with {} and {}", services, signedInUser);

        super.init(services, signedInUser);
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

        Configuration.logger.traceExit();
    }

    private void loadGameTableData() {
        Configuration.logger.traceEntry();

        try {
            if (switchFilterCheckBox.isSelected())
                games = services.getGamesWithAvailableSeatsDescending();
            else {
                games = services.getAllGames();
            }
            gameObservableList.setAll(games);
        } catch (Exception exception) {
            Configuration.logger.catching(exception);

            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }

        Configuration.logger.traceExit();
    }

    @FXML
    public void sellSeats() {
        Configuration.logger.traceEntry();

        try {
            if (gamesTable.getSelectionModel().getSelectedItem() == null)
                throw new NotFoundException("no game selected");
            services.sellSeats(gamesTable.getSelectionModel().getSelectedItem(), clientNameField.getText(), seatsCountSpinner.getValue());
            gamesTable.getSelectionModel().clearSelection();
            clientNameField.clear();
            seatsCountSpinner.getValueFactory().setValue(1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "seats sold successfully");
            alert.show();
        } catch (Exception exception) {
            Configuration.logger.catching(exception);

            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }

        Configuration.logger.traceExit();
    }

    @FXML
    private void showGamesFiltered() {
        Configuration.logger.traceEntry();

        loadGameTableData();

        Configuration.logger.traceExit();
    }

    @FXML
    public void signOut() {
        Configuration.logger.traceEntry();

        try {
            services.signOutUser(signedInUser.getUsername(), this);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "logged out successfully");
            alert.show();
            ((Stage) welcomeText.getScene().getWindow()).close();
        } catch (Exception exception) {
            Configuration.logger.catching(exception);

            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }

        Configuration.logger.traceExit();
    }

    @Override
    public void seatsSold(Integer gameId, Integer seatsCount) {
        Configuration.logger.traceEntry("entering with {} and {}", gameId, seatsCount);

        for (Game searchedGame : games) {
            if (searchedGame.getId().equals(gameId)) {
                searchedGame.setAvailableSeats(searchedGame.getAvailableSeats() - seatsCount);
                break;
            }
        }
        gameObservableList.setAll(games);

        Configuration.logger.traceExit();
    }
}
