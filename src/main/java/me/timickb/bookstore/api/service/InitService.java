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

@Service
public class InitService {
    private final ApplicationArguments arguments;
    private final BookRepository bookRepo;
    private final AccountRepository accountRepo;

    @Autowired
    public InitService(ApplicationArguments arguments, BookRepository bookRepo, AccountRepository accountRepo) {
        this.arguments = arguments;
        this.bookRepo = bookRepo;
        this.accountRepo = accountRepo;
    }

    public void initDatabaseFromFile() {
        if (arguments.getSourceArgs().length == 0) {
            return;
        }

        String filename = arguments.getSourceArgs()[1];

        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            Gson gson = new Gson();

            InitData data = gson.fromJson(reader, InitData.class);

            accountRepo.saveAll(data.getAccounts());
            bookRepo.saveAll(data.getBooks());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
