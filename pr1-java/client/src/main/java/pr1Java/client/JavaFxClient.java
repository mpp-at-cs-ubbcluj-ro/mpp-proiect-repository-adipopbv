package pr1Java.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pr1Java.client.controllers.SignInController;
import pr1Java.networking.rpcProtocol.ServicesRpcProxy;
import pr1Java.services.IServices;

public class JavaFxClient extends Application {

    private static final int defaultPort = 55555;
    private static final String defaultServer = "localhost";

    public static void main(String[] args) {
        Configuration.loadProperties("./client/client.config");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String serverIp = Configuration.properties.getProperty("server.host", defaultServer);
        Integer serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(Configuration.properties.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            Configuration.logger.error("Wrong port number " + ex.getMessage());
            Configuration.logger.warn("Using default port: " + defaultPort);
        }
        Configuration.logger.info("Using server IP " + serverIp);
        Configuration.logger.info("Using server port " + serverPort);

        IServices server = new ServicesRpcProxy(serverIp, serverPort);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/signInWindow.fxml"));
        Parent root = loader.load();
        ((SignInController) loader.getController()).init(server, null);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log In");
        primaryStage.show();
    }
}
