package com.example.aadbackspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AadBackSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(AadBackSpringApplication.class, args);
    }

}