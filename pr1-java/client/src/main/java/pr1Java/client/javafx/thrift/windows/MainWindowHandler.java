package pr1Java.client.javafx.thrift.windows;

import org.apache.thrift.TException;
import pr1Java.client.Configuration;
import pr1Java.services.thrift.ThriftClient;
import pr1Java.services.thrift.datatransfer.GameDto;

public class MainWindowHandler implements ThriftClient.Iface {
    MainWindow mainWindow;

    public MainWindowHandler(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void seatsSold(int gameId, int seatsCount) throws TException {
        Configuration.logger.traceEntry("entering with {} and {}", gameId, seatsCount);

        for (GameDto searchedGame : mainWindow.games) {
            if (searchedGame.getGameId() == gameId) {
                searchedGame.setAvailableSeats(searchedGame.getAvailableSeats() - seatsCount);
                break;
            }
        }
        mainWindow.gameObservableList.setAll(mainWindow.games);

        Configuration.logger.traceExit();
    }
}
