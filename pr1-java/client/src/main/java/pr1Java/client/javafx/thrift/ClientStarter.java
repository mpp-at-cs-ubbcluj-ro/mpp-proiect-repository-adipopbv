package pr1Java.client.javafx.thrift;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import pr1Java.client.Configuration;
import pr1Java.client.javafx.thrift.windows.SignInWindow;
import pr1Java.services.thrift.ThriftServices;

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

        TTransport connection = new TSocket(serverIp, serverPort);
        connection.open();
        var services = new ThriftServices.Client(new TBinaryProtocol(connection));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/thrift/signInWindow.fxml"));
        Parent root = loader.load();
        ((SignInWindow) loader.getController()).init(connection, services, null);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log In");
        primaryStage.show();

        Configuration.logger.traceExit();
    }
}
