package me.timickb.bookstore.api.service.implementation;

import me.timickb.bookstore.api.Application;
import me.timickb.bookstore.api.mapper.AccountMapper;
import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;
    private final ValidationService validationService;
    private final AccountMapper mapper;
    private final LoggingService logger;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepo, ValidationService validationService,
                          AccountMapper mapper, LoggingService logger) {
        this.accountRepo = accountRepo;
        this.validationService = validationService;
        this.mapper = mapper;
        this.logger = logger;
    }

    public List<AccountResponse> getAll() {
        return accountRepo.findAll().stream()
                .map(mapper::accountToResponse).collect(Collectors.toList());
    }

    public List<AccountResponse> getPageable(int page, int limit) {
        return accountRepo.findAll(PageRequest.of(page, limit)).stream()
                .map(mapper::accountToResponse).collect(Collectors.toList());
    }

    public Optional<AccountResponse> getById(long id) {
        Optional<Account> accountOptional = accountRepo.findById(id);
        if (accountOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.accountToResponse(accountOptional.get()));
    }

    public PostResponse create(final AccountAddRequest request) {
        PostResponse response = new PostResponse();
        Optional<Account> existing = accountRepo.findByEmail(request.getEmail());

        if (existing.isPresent()) {
            response.setMessage("This email is already registered");
            return response;
        }

        String checkResult = validationService.validateAccountAddRequest(request);
        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            response.setMessage(checkResult);
            return response;
        }

        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder(Application.BCRYPT_STRENGTH, new SecureRandom());

        Account account = new Account();
        copyProperties(request, account);
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        accountRepo.saveAndFlush(account);

        logger.info("Created account with id %d".formatted(account.getId()));
        response.setSucceeded(true);
        response.setMessage("Account added!");
        return response;
    }

    public PostResponse update(final AccountAddRequest edited, long id) {
        PostResponse response = new PostResponse();

        String checkResult = validationService.validateAccountAddRequest(edited);
        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            response.setMessage(checkResult);
            return response;
        }

        Optional<Account> existing = accountRepo.findById(id);
        if (existing.isEmpty()) {
            response.setMessage("Account with id %d doesn't exist".formatted(id));
            return response;
        }

        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder(Application.BCRYPT_STRENGTH, new SecureRandom());

        Account account = existing.get();
        copyProperties(edited, account);
        account.setPasswordHash(passwordEncoder.encode(edited.getPassword()));

        accountRepo.saveAndFlush(account);

        logger.info("Updated account with id %d".formatted(account.getId()));
        response.setMessage("Account updated!");
        response.setSucceeded(true);
        return response;
    }

    public PostResponse delete(long id) {
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
