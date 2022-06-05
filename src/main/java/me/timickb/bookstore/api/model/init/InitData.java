package me.timickb.bookstore.api.model.init;

import lombok.Data;
import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.request.BookAddRequest;

import java.io.Serializable;
import java.util.List;


@Data
public class InitData implements Serializable {
    private List<BookCategory> categories;
    private List<Book> books;
    private List<Account> accounts;
}
