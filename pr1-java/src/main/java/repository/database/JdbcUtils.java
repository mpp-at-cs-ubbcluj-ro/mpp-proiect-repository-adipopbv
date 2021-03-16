package repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private final Properties properties;
    private Connection instance = null;
    private static final Logger logger = LogManager.getLogger();

    public JdbcUtils(Properties properties) {
        this.properties = properties;
    }

    private Connection getNewConnection() {
        logger.traceEntry();

        String url = properties.getProperty("jdbc.url");
        logger.info("Trying to connect to database... {}", url);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            connection.prepareStatement("pragma foreign_keys = on;").executeUpdate();
            logger.info("Connected successfully to {}", url);
        } catch (SQLException exception) {
            logger.error(exception);
            System.out.println("Error getting connection | " + exception);
        }

        logger.traceExit(connection);
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();

        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException exception) {
            logger.error(exception);
            System.out.println("Error connecting to db | " + exception);
        }

        logger.traceExit(instance);
        return instance;
    }
}
