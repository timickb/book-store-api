package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deals")
public class DealController {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_LIMIT = 100;

    private final DealService dealService;

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping
    public ResponseEntity<List<Deal>> getDeals(@RequestParam("page") Optional<Integer> page,
                                               @RequestParam("limit") Optional<Integer> limit) {
        if (page.isEmpty()) page = Optional.of(DEFAULT_PAGE);
        if (limit.isEmpty()) limit = Optional.of(DEFAULT_PAGE_LIMIT);
        return ResponseEntity.ok(dealService.getDealsPageable(page.get(), limit.get()));
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
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{dealId}")
    public ResponseEntity<PostResponse> deleteDeal(@PathVariable Long dealId) {
        PostResponse response = dealService.deleteDeal(dealId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
