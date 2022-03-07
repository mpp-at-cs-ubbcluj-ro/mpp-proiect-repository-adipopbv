package pr1Java.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pr1Java.client.clients.SignInClient;
import pr1Java.services.IServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JavaFxRpcClientStarter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Configuration.logger.traceEntry();

        Configuration.loadProperties("./client/client.config");
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");

        IServices services = (IServices) factory.getBean("services");
        Configuration.logger.trace("connected to {} instance", services);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/signInWindow.fxml"));
        Parent root = loader.load();
        ((SignInClient) loader.getController()).init(services, null);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log In");
        primaryStage.show();

        Configuration.logger.traceExit();
    }
}
