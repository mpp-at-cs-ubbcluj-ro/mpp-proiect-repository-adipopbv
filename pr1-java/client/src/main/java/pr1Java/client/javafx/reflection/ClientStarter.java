package pr1Java.client.javafx.reflection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pr1Java.client.Configuration;
import pr1Java.client.javafx.reflection.clients.SignInWindow;
import pr1Java.networking.reflection.ServicesRpcProxy;
import pr1Java.services.reflection.ReflectionServices;

public class ClientStarter extends Application {

    private static final int defaultPort = 55555;
    private static final String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Configuration.logger.traceEntry();

        Configuration.loadProperties("./client/client.config");

        String serverIp = Configuration.properties.getProperty("server.host", defaultServer);
        Integer serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(Configuration.properties.getProperty("server.port"));
        } catch (NumberFormatException exception) {
            Configuration.logger.error("wrong port number {}", exception.getMessage());
            Configuration.logger.warn("using default port {}", defaultPort);
        }
        Configuration.logger.info("using server ip " + serverIp);
        Configuration.logger.info("using server port " + serverPort);

        ReflectionServices services = new ServicesRpcProxy(serverIp, serverPort);
        Configuration.logger.trace("created {} instance", services);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/signInWindow.fxml"));
        Parent root = loader.load();
        ((SignInWindow) loader.getController()).init(services, null);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log In");
        primaryStage.show();

        Configuration.logger.traceExit();
    }
}