package com.devillage.teamproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevillageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevillageApplication.class, args);
    }

}
