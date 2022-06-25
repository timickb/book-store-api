package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.CategoryRepository;
import me.timickb.bookstore.api.repository.DealRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService service;

    @MockBean
    private BookRepository bookRepo;
    @MockBean
    private CategoryRepository categoryRepo;

    @Test
    void getAllBooks() {
        List<Book> books = service.getAllBooks();
        Assertions.assertNotNull(books);
        Mockito.verify(bookRepo, Mockito.times(1)).findByAmountGreaterThan(0);
    }

    @Test
    void getBookById() {
        Mockito.doReturn(Optional.of(new Book())).when(bookRepo).findById(1L);

        Optional<Book> book = service.getBookById(1);

        Assertions.assertTrue(book.isPresent());
        Mockito.verify(bookRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    void createBook() {
        Mockito.doReturn(Optional.of(new BookCategory())).when(categoryRepo).findById(1L);
        BookAddRequest book = new BookAddRequest();
        book.setName("test");
        book.setAuthor("test");
        book.setAmount(1);
        book.setPrice(1);
        book.setCategoryId(1);

        PostResponse response = service.createBook(book);
        System.out.println(response.getMessage());
        Assertions.assertTrue(response.isSucceeded());

        Mockito.verify(bookRepo, Mockito.times(1))
                .saveAndFlush(ArgumentMatchers.any());
    }

    @Test
    void createBookInvalidCategory() {
        BookAddRequest book = new BookAddRequest();
        book.setName("test");
        book.setAuthor("test");
        book.setAmount(1);
        book.setPrice(1);
        book.setCategoryId(999999);

        PostResponse response = service.createBook(book);
        System.out.println(response.getMessage());
        Assertions.assertFalse(response.isSucceeded());
    }

    @Test
    void deleteBook() {
        Book book = new Book();
        Mockito.doReturn(Optional.of(book)).when(bookRepo).findById(1L);
        PostResponse response = service.deleteBook(1);

        Assertions.assertTrue(response.isSucceeded());
        Mockito.verify(bookRepo, Mockito.times(1)).delete(book);
    }
}