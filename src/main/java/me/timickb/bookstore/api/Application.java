package me.timickb.bookstore.api;

import me.timickb.bookstore.api.service.InitService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        // Load data from file.
        context.getBean(InitService.class).initDatabaseFromFile();
    }
}
