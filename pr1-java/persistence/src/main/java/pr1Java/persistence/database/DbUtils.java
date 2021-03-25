package pr1Java.persistence.database;

import pr1Java.persistence.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    private static Connection connection = null;

    public static Connection getConnection() {
        Configuration.logger.traceEntry();

        try {
            if (connection == null || connection.isClosed())
                connection = getNewConnection();
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
            System.out.println("Error connecting to db | " + exception);
        }

        Configuration.logger.traceExit(connection);
        return connection;
    }

    private static Connection getNewConnection() {
        Configuration.logger.traceEntry();

        String url = Configuration.properties.getProperty("jdbc.url");
        Configuration.logger.info("Trying to connect to database... {}", url);
        Connection newConnection = null;
        try {
            newConnection = DriverManager.getConnection(url);
            newConnection.prepareStatement("pragma foreign_keys = on;").executeUpdate();
            Configuration.logger.info("Connected successfully to {}", url);
        } catch (SQLException exception) {
            Configuration.logger.error(exception);
        }

        Configuration.logger.traceExit(newConnection);
        return newConnection;
    }
}
