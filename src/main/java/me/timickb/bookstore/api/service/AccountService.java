package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.mapper.ResponseMapper;
import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepo;
    private final ResponseMapper mapper;
    private final LoggingService logger;

    @Autowired
    public AccountService(AccountRepository accountRepo, ResponseMapper mapper, LoggingService logger) {
        this.accountRepo = accountRepo;
        this.mapper = mapper;
        this.logger = logger;
    }

    public List<AccountResponse> getAllAccounts() {
        //noinspection OptionalGetWithoutIsPresent
        return accountRepo.findAll().stream()
                .map(a -> getById(a.getId()).get())
                .collect(Collectors.toList());
    }

    public Optional<AccountResponse> getById(long id) {
        Optional<Account> accountOptional = accountRepo.findById(id);
        if (accountOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.responseFromAccount(accountOptional.get()));
    }

    public PostResponse createAccount(Account account) {
        accountRepo.saveAndFlush(account);
        logger.info("Created account with id %d".formatted(account.getId()));
        PostResponse response = new PostResponse();
        response.setSucceeded(true);
        response.setMessage("Account added!");
        return response;
    }

    public PostResponse updateAccount(Account edited, long id) {
        PostResponse response = new PostResponse();
        Optional<Account> existing = accountRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Account with id %d doesn't exist".formatted(id));
            return response;
        }

        Account account = existing.get();
        account.setBalance(edited.getBalance());
        accountRepo.saveAndFlush(account);

        logger.info("Updated account with id %d".formatted(account.getId()));
        response.setMessage("Account updated!");
        response.setSucceeded(true);
        return response;
    }

    public PostResponse deleteAccount(long id) {
        PostResponse response = new PostResponse();
        Optional<Account> existing = accountRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Account with id %d doesn't exist".formatted(id));
            return response;
        }

        accountRepo.delete(existing.get());

        logger.info("Deleted account with id %d".formatted(id));
        response.setMessage("Account deleted");
        response.setSucceeded(true);
        return response;
    }

}
