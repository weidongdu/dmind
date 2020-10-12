package info.dmind.dmind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DmindApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmindApplication.class, args);
    }

}
