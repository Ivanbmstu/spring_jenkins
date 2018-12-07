package ru.alfabank.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InfraApplication {
    public static void main(String[] args) {

        final var context = SpringApplication.run(InfraApplication.class, args);
        final int x = context.hashCode();
        System.out.println(x);
    }
}