package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.BookFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    private final ValidationService service = new ValidationService();

    @Test
    void validateCategoryFailed() {
        BookCategory category = new BookCategory();
        category.setName("a".repeat(120));
        assertEquals(ValidationService.CATEGORY_NAME_FAILED_MSG, service.validateCategory(category));
    }

    @Test
    void validateAccountAddRequest() {
        AccountAddRequest request = new AccountAddRequest();
        request.setPassword("12345678");
        request.setEmail("ricardo@milos.mem");
        assertEquals(ValidationService.SUCCESS_MSG, service.validateAccountAddRequest(request));
    }

    @Test
    void validateBookFilterFailed() {
        BookFilter filter = new BookFilter();
        filter.setSortPrice("invalid sort type");
        assertEquals(ValidationService.BOOK_FILTER_SORTING_FAILED_MSG, service.validateBookFilter(filter));
    }


    @Test
    void validateBook() {
        Book book = new Book();
        book.setPrice(-10);
        assertEquals(ValidationService.BOOK_PRICE_FAILED_MSG, service.validateBook(book));
    }
}