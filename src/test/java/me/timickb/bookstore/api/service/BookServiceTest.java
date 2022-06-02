package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.DealRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService service;

    @MockBean
    private BookRepository bookRepo;
    @MockBean
    private AccountRepository accountRepo;
    @MockBean
    private DealRepository dealRepo;

    @Test
    void getAllBooks() {
        List<Book> books = service.getAllBooks();
        Assertions.assertNotNull(books);
    }

    @Test
    void getBookById() {
        Book test = new Book();
        test.setAmount(1);
        test.setAuthor("Test");
        test.setName("Test");
        test.setPrice(10);
        service.createBook(test);
        Optional<Book> book = service.getBookById(1);
        List<Book> books = service.getAllBooks();
        System.out.println(books);
        Assertions.assertTrue(book.isPresent());
    }

    @Test
    void createBook() {
        Book book = new Book();
        book.setAmount(1);
        book.setAuthor("Test");
        book.setName("Test");
        book.setPrice(10);

        PostResponse response = service.createBook(book);
        Assertions.assertTrue(response.isSucceeded());
    }

    @Test
    void makeDeal() {

        DealRequest request = new DealRequest();
    }
}