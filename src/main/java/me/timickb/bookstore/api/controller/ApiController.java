package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.AccountService;
import me.timickb.bookstore.api.service.InitService;
import me.timickb.bookstore.api.service.MarketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ApiController {

    private final AccountService accountService;
    private final MarketService marketService;

    private final Logger logger = LoggerFactory.getLogger(InitService.class);

    @Autowired
    public ApiController(AccountService accountService, MarketService marketService) {
        this.accountService = accountService;
        this.marketService = marketService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        try {
            return ResponseEntity.ok(accountService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        Optional<AccountResponse> response = accountService.getById(accountId);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PostMapping("/accounts")
    public ResponseEntity<PostResponse> createAccount(@RequestBody Account account) {
        PostResponse response = accountService.create(account);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/market")
    public ResponseEntity<PostResponse> createBook(@RequestBody Book book) {
        PostResponse response = marketService.createBook(book);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }


    @GetMapping("/market")
    public ResponseEntity<List<Book>> getMarket() {
        return ResponseEntity.ok(marketService.getAllBooks());
    }

    @GetMapping("/market/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        Optional<Book> book = marketService.getBookById(bookId);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @PostMapping("/market/deal")
    public ResponseEntity<PostResponse> makeDeal(@RequestBody DealRequest request) {
        PostResponse response = marketService.makeDeal(request);
        if (response.isSucceeded()) {
            logger.info("Account %d bought the book %d (%d items)"
                    .formatted(request.getAccountId(), request.getBookId(), request.getAmount()));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
