package me.timickb.bookstore.api.service.implementation;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.BookFilter;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static org.springframework.beans.BeanUtils.copyProperties;

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

    public static final String BOOK_FILTER_SORTING_FAILED_MSG = "Invalid sorting. Use ASC or DESC";

    public static final String CATEGORY_NAME_FAILED_MSG = "Name length limit is 100 chars";

    public static final String ACCOUNT_SHORT_PASSWORD = "Password should be at least 8 characters long";
    public static final String ACCOUNT_INVALID_EMAIL = "Email format is invalid";

    public static final int CATEGORY_NAME_LIMIT = 100;
    public static final int MIN_PASSWORD_LENGTH = 8;

    private boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }


    public String validateCategory(BookCategory category) {
        if (category.getName().length() > CATEGORY_NAME_LIMIT) {
            return CATEGORY_NAME_FAILED_MSG;
        }
        return SUCCESS_MSG;
    }

    public String validateAccountAddRequest(AccountAddRequest account) {
        if (account.getPassword().length() < MIN_PASSWORD_LENGTH) {
            return ACCOUNT_SHORT_PASSWORD;
        }

        if (!validateEmail(account.getEmail())) {
            return ACCOUNT_INVALID_EMAIL;
        }
        return SUCCESS_MSG;
    }

    public String validateBookFilter(BookFilter filter) {
        String sorting = filter.getSortPrice();
        if (sorting == null || sorting.equals("ASC") || sorting.equals("DESC")) {
            return SUCCESS_MSG;
        }
        return BOOK_FILTER_SORTING_FAILED_MSG;
    }

    public String validateBookAddRequest(BookAddRequest request) {
        Book book = new Book();
        copyProperties(request, book);
        return validateBook(book);
    }

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
