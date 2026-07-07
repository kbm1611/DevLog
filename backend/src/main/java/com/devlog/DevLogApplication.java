package com.devlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevLogApplication.class, args);
    }
}
