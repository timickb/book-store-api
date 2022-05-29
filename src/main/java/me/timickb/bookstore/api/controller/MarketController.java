package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.LoggingService;
import me.timickb.bookstore.api.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/market")
public class MarketController {
    private final MarketService marketService;
    private final LoggingService logger;

    @Autowired
    public MarketController(MarketService marketService, LoggingService logger) {
        this.marketService = marketService;
        this.logger = logger;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createBook(@RequestBody Book book) {
        PostResponse response = marketService.createBook(book);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getMarket() {
        return ResponseEntity.ok(marketService.getAllBooks());
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        Optional<Book> book = marketService.getBookById(bookId);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @PutMapping("{bookId}")
    public ResponseEntity<PostResponse> updateBook(@RequestBody Book edited,
                                                   @PathVariable Long bookId) {
        PostResponse response = marketService.editBook(edited, bookId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<PostResponse> deleteBook(@PathVariable Long bookId) {
        PostResponse response = marketService.deleteBook(bookId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("deal")
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
