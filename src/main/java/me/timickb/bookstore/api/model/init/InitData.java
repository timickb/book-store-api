package me.timickb.bookstore.api.model.init;

import lombok.Data;
import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;

import java.io.Serializable;
import java.util.List;


@Data
public class InitData implements Serializable {
    private List<BookCategory> categories;
    private List<Book> books;
    private List<Account> accounts;
}
