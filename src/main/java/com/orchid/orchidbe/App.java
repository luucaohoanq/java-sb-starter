package com.orchid.orchidbe;

import io.github.lcaohoanq.annotations.BrowserLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.orchid.orchidbe.repositories")
@EntityScan(basePackages = "com.orchid.orchidbe.domain")
@BrowserLauncher(
    value = "http://localhost:8080/swagger-ui.html",
    healthCheckEndpoint = "http://localhost:8080/actuator/health"
)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
