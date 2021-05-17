package pr1Java.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("pr1Java")
@SpringBootApplication
public class SpringBootServerStarter {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootServerStarter.class, args);
    }
}
