package me.timickb.bookstore.api.service;

import com.google.gson.Gson;
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

    @Autowired
    public InitService(ApplicationArguments arguments, BookRepository bookRepo,
                       AccountRepository accountRepo, Gson gson) {
        this.arguments = arguments;
        this.bookRepo = bookRepo;
        this.accountRepo = accountRepo;
        this.gson = gson;
    }

    /**
     * Reads and parses json file passed in first argument.
     *
     * @return true: parsed successfully; false: file doesn't exist;
     * argument wasn't passed; json parse error occurred.
     */
    public boolean initDatabaseFromFile() {
        if (arguments.getSourceArgs().length == 0) {
            return false;
        }

        String filename = arguments.getSourceArgs()[0];

        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            InitData data = gson.fromJson(reader, InitData.class);

            accountRepo.saveAllAndFlush(data.getAccounts());
            bookRepo.saveAllAndFlush(data.getBooks());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
