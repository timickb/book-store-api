package me.timickb.bookstore.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "me.timickb.bookstore.api",
        "me.timickb.bookstore.api.repository",
        "me.timickb.bookstore.api.model",
        "me.timickb.bookstore.api.mapper",
        "me.timickb.bookstore.api.controller",
        "me.timickb.bookstore.api.service"
})
public class Application {

    public static final int BCRYPT_STRENGTH = 10;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
