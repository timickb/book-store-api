package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
    }

    @Test
    void getById() {
        Optional<AccountResponse> optional = accountService.getById(1);
        Assertions.assertNotNull(optional);
    }

    @Test
    void create() {
        Account account = new Account();
        account.setBalance(100);
        PostResponse response = accountService.create(account);
        Assertions.assertTrue(response.isSucceeded());
    }
}