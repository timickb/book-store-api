package me.timickb.bookstore.api.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import me.timickb.bookstore.api.model.init.InitData;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Responsible for initial database content handling
 * from passed json file (as command line argument)
 */
@Service
public class InitService {
    private final ApplicationArguments arguments;
    private final BookRepository bookRepo;
    private final AccountRepository accountRepo;
    private final Gson gson;
    private final LoggingService logger;

    @Autowired
    public InitService(ApplicationArguments arguments, BookRepository bookRepo,
                       AccountRepository accountRepo, Gson gson, LoggingService logger) {
        this.arguments = arguments;
        this.bookRepo = bookRepo;
        this.accountRepo = accountRepo;
        this.gson = gson;
        this.logger = logger;
    }

    /**
     * Reads and parses json file passed in first argument.
     */
    public void initDatabaseFromFile() {
        if (arguments.getSourceArgs().length == 0) {
            logger.info("Initial JSON file not specified, skip reading data");
            return;
        }

        String filename = arguments.getSourceArgs()[0];
        logger.info("Detected initial data file " + filename + ", trying to read it...");

        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            InitData data = gson.fromJson(reader, InitData.class);

            accountRepo.saveAllAndFlush(data.getAccounts());
            bookRepo.saveAllAndFlush(data.getBooks());

            logger.info("Initial data successfully parsed and pushed into database!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Unable to read initial data file: file not found");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            logger.error("Unable to read initial data file: invalid JSON syntax");
        }
    }
}
