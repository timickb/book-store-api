package me.timickb.bookstore.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

@Service
public class LoggingService {
    private final ApplicationArguments arguments;
    private final Logger logger = LoggerFactory.getLogger(InitService.class);

    @Autowired
    public LoggingService(ApplicationArguments arguments) {
        this.arguments = arguments;
    }

    private void logToFile(String level, String message) {
        if (arguments.getSourceArgs().length < 2) {
            return;
        }

        String filename = arguments.getSourceArgs()[1];
        String dateTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
                .format(new java.util.Date());
        String data = "[%s] [%s] %s".formatted(level, dateTime, message);

        try {
            FileWriter fileWriter = new FileWriter(filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(data);
            printWriter.close();
        } catch (IOException ignored) {
        }

    }

    public void debug(String message) {
        logger.debug(message);
        logToFile("DEBUG", message);
    }

    public void info(String message) {
        logger.info(message);
        logToFile("INFO", message);

    }

    public void error(String message) {
        logger.error(message);
        logToFile("ERROR", message);

    }

    public void warn(String message) {
        logger.warn(message);
        logToFile("WARNING", message);
    }
}
