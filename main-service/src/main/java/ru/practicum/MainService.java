package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainService {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MainService.class);
        app.run(args);
    }
}