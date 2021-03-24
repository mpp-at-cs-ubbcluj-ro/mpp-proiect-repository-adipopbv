package pr1Java.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    public static Properties properties;
    public static Logger logger = LogManager.getLogger();

    public static void loadProperties(String configurationFile) {
        properties = new Properties();
        try {
            properties.load(new FileReader(configurationFile));
        } catch (IOException exception) {
            logger.error(exception);
        }
    }
}
