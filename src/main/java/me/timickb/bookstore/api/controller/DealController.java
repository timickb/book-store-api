package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.BookService;
import me.timickb.bookstore.api.service.DealService;
import me.timickb.bookstore.api.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deals")
public class DealController {
    private final DealService dealService;
    private final LoggingService logger;

    @Autowired
    public DealController(DealService dealService, LoggingService logger) {
        this.dealService = dealService;
        this.logger = logger;
    }

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {
        return ResponseEntity.ok(dealService.getAllDeals());
    }

    @GetMapping("{dealId}")
    public ResponseEntity<Deal> getDeal(@PathVariable Long dealId) {
        Optional<Deal> deal = dealService.getDealById(dealId);
        if (deal.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deal.get());
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<List<Deal>> getAllDealsForAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(dealService.getAllForAccount(accountId));
    }

    @GetMapping("book/{bookId}")
    public ResponseEntity<List<Deal>> getAllDealsForBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(dealService.getAllForBook(bookId));
    }

    @PostMapping
    public ResponseEntity<PostResponse> makeDeal(@RequestBody DealRequest request) {
        PostResponse response = dealService.makeDeal(request);
        if (response.isSucceeded()) {
            logger.info("Account %d bought the book %d (%d items)"
                    .formatted(request.getAccountId(), request.getBookId(), request.getAmount()));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
