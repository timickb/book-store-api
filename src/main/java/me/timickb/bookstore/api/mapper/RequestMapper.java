package me.timickb.bookstore.api.mapper;

import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Maps request entities to base entities.
 */
@Component
public class RequestMapper {
    private final AccountRepository accountRepo;
    private final BookRepository bookRepo;

    @Autowired
    public RequestMapper(AccountRepository accountRepo, BookRepository bookRepo) {
        this.accountRepo = accountRepo;
        this.bookRepo = bookRepo;
    }

}
