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


    @Autowired
    public AccountService(AccountRepository accountRepo, ResponseMapper mapper) {
        this.accountRepo = accountRepo;
        this.mapper = mapper;
    }

    public List<AccountResponse> getAll() {
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

    public PostResponse create(Account account) {
        accountRepo.saveAndFlush(account);
        PostResponse response = new PostResponse();
        response.setSucceeded(true);
        response.setMessage("Account added!");
        return response;
    }

    public PostResponse edit(Account edited, long id) {
        PostResponse response = new PostResponse();
        Optional<Account> existing = accountRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Account with id %d doesn't exist".formatted(id));
            return response;
        }

        Account account = existing.get();
        account.setBalance(edited.getBalance());
        accountRepo.saveAndFlush(account);

        response.setMessage("Account updated!");
        response.setSucceeded(true);
        return response;
    }

}
