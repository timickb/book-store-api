package me.timickb.bookstore.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

/**
 * Wrapper for org.slf4j.Logger
 */
@Service
public class LoggingService {
    private final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    @Autowired
    public LoggingService(ApplicationArguments arguments) {
    }


    public void debug(String message) {
        logger.debug(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }
}
