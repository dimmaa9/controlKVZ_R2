package kvz.zsu.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ControlApplication {

    public static void main(String[] args) {

        System.out.println(new BCryptPasswordEncoder().encode("admin"));
        SpringApplication.run(ControlApplication.class, args);
    }

}
