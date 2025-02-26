package ir.maktabsharif.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"ir.maktabsharif.webapplication.service","ir.maktabsharif.webapplication.repository",
"ir.maktabsharif.webapplication.exception","ir.maktabsharif.webapplication.configuration"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
