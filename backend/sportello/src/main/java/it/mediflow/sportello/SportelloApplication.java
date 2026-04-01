package it.mediflow.sportello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan( basePackages = { "it.mediflow.sportello.entity" } )
@EnableJpaRepositories( basePackages = { "it.mediflow.sportello.repository" } )
public class SportelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportelloApplication.class, args);
    }

}
