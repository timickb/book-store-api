package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.Application;
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
public class DealController implements CrudController<Deal, DealRequest> {

    private final DealService dealService;

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping
    public ResponseEntity<List<Deal>> readList(@RequestParam("page") Optional<Integer> page,
                                               @RequestParam("limit") Optional<Integer> limit) {
        if (page.isEmpty()) page = Optional.of(Application.DEFAULT_PAGE);
        if (limit.isEmpty()) limit = Optional.of(Application.DEFAULT_PAGE_LIMIT);
        return ResponseEntity.ok(dealService.getDealsPageable(page.get(), limit.get()));
    }

    @GetMapping("{dealId}")
    public ResponseEntity<Deal> readOne(@PathVariable Long dealId) {
        Optional<Deal> deal = dealService.getDealById(dealId);
        if (deal.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deal.get());
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody DealRequest request) {
        PostResponse response = dealService.makeDeal(request);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    public ResponseEntity<PostResponse> update(DealRequest request, Long id) {
        return null;
    }

    @DeleteMapping("{dealId}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long dealId) {
        PostResponse response = dealService.deleteDeal(dealId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<List<Deal>> getAllDealsForAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(dealService.getAllForAccount(accountId));
    }

    @GetMapping("book/{bookId}")
    public ResponseEntity<List<Deal>> getAllDealsForBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(dealService.getAllForBook(bookId));
    }
}
