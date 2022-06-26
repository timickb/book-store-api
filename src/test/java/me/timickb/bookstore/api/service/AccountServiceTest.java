package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepo;

    @Test
    void getAll() {
        List<AccountResponse> accounts = accountService.getAll();
        Assertions.assertNotNull(accounts);
        Mockito.verify(accountRepo, Mockito.times(1)).findAll();
    }

    @Test
    void getById() {
        Account existing = new Account();
        existing.setId(1);

        Mockito.doReturn(Optional.of(existing)).when(accountRepo).findById(1L);

        Optional<AccountResponse> optional = accountService.getById(1);
        Assertions.assertTrue(optional.isPresent());
    }

    @Test
    void create() {
        AccountAddRequest account = new AccountAddRequest();
        account.setBalance(100);
        account.setEmail("somebody@somewhere.com");
        account.setPassword("12345678");
        PostResponse response = accountService.create(account);
        Assertions.assertTrue(response.isSucceeded());

        Mockito.verify(accountRepo, Mockito.times(1))
                .saveAndFlush(ArgumentMatchers.any());
    }

    @Test
    void createFailedPassword() {
        AccountAddRequest account = new AccountAddRequest();
        account.setBalance(100);
        account.setEmail("somebody@somewhere.com");
        account.setPassword("12345");
        PostResponse response = accountService.create(account);
        Assertions.assertFalse(response.isSucceeded());
    }

    @Test
    void createFailedEmail() {
        AccountAddRequest account = new AccountAddRequest();
        account.setBalance(100);
        account.setEmail("invalid email");
        account.setPassword("12345678");
        PostResponse response = accountService.create(account);
        Assertions.assertFalse(response.isSucceeded());
    }

    @Test
    void createExistingEmail() {
        Mockito.doReturn(Optional.of(new Account()))
                .when(accountRepo)
                .findByEmail("somebody@somewhere.com");

        AccountAddRequest account = new AccountAddRequest();

        account.setBalance(100);
        account.setEmail("somebody@somewhere.com");
        account.setPassword("12345678");

        PostResponse response = accountService.create(account);
        Assertions.assertFalse(response.isSucceeded());

        Mockito.verify(accountRepo, Mockito.times(0))
                .saveAndFlush(ArgumentMatchers.any());
    }

    @Test
    void delete() {
        Mockito.doReturn(Optional.of(new Account()))
                .when(accountRepo)
                .findById(1L);
        PostResponse response = accountService.delete(1);

        Assertions.assertTrue(response.isSucceeded());
        Mockito.verify(accountRepo, Mockito.times(1))
                .delete(ArgumentMatchers.any());
    }
}