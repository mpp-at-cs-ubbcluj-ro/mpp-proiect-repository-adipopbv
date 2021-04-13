package pr1Java.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcServerStarter {

    public static void main(String[] args) {
        Configuration.logger.traceEntry();

        Configuration.loadProperties("./server/server.config");
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-server.xml");

        Configuration.logger.traceExit();
    }
}
