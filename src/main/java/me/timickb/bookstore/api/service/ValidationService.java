package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Book;
import org.springframework.stereotype.Service;

/**
 * Responsible for entities fields validation.
 */
@Service
public class ValidationService {
    public static final String SUCCESS_MSG = "";

    public static final String BOOK_PRICE_FAILED_MSG = "Price should be positive";
    public static final String BOOK_AMOUNT_FAILED_MSG = "Amount should be non-negative";
    public static final String BOOK_AUTHOR_FAILED_MSG = "Author name shouldn't be empty";
    public static final String BOOK_NAME_FAILED_MSG = "Book name shouldn't be empty";

    public String validateBook(Book book) {
        if (book.getPrice() <= 0) {
            return BOOK_PRICE_FAILED_MSG;
        }
        if (book.getAmount() < 0) {
            return BOOK_AMOUNT_FAILED_MSG;
        }
        if (book.getAuthor().isEmpty()) {
            return BOOK_AUTHOR_FAILED_MSG;
        }
        if (book.getName().isEmpty()) {
            return BOOK_NAME_FAILED_MSG;
        }
        return SUCCESS_MSG;
    }

}
