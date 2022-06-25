package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.DealRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DealServiceTest {
    @Autowired
    private DealService dealService;

    @MockBean
    private DealRepository dealRepo;
    @MockBean
    private AccountRepository accountRepo;
    @MockBean
    private BookRepository bookRepo;
    @Test
    public void deleteDeal() {
        Deal deal = new Deal();
        Mockito.doReturn(Optional.of(deal)).when(dealRepo).findById(1L);

        PostResponse resp = dealService.deleteDeal(1);

        Assertions.assertTrue(resp.isSucceeded());
        Mockito.verify(dealRepo, Mockito.times(1)).delete(deal);
    }

    @Test
    public void getAllDeals() {
        List<Deal> deals = dealService.getDealsPageable(0, 100);
        Assertions.assertNotNull(deals);
        Mockito.verify(dealRepo, Mockito.times(1)).findAll();
    }

    @Test
    public void getDealById() {
        Mockito.doReturn(Optional.of(new Deal())).when(dealRepo).findById(1L);
        Optional<Deal> optional = dealService.getDealById(1);

        Assertions.assertTrue(optional.isPresent());
        Mockito.verify(dealRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getAllForAccount() {

    }

    @Test
    public void getAllForBook() {
    }

    @Test
    public void makeDeal() {
        DealRequest request = new DealRequest();
        request.setAmount(1);
        request.setBookId(1);
        request.setAccountId(1);

        Book book = new Book();
        book.setPrice(100);
        book.setAmount(5);

        Account account = new Account();
        account.setBalance(1000);

        Mockito.doReturn(Optional.of(book)).when(bookRepo).findById(1L);
        Mockito.doReturn(Optional.of(account)).when(accountRepo).findById(1L);

        PostResponse response = dealService.makeDeal(request);

        System.out.println(account.getBalance());
    }
}
