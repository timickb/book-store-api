package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        try {
            return ResponseEntity.ok(accountService.getAllAccounts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        Optional<AccountResponse> response = accountService.getById(accountId);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PostMapping
    public ResponseEntity<PostResponse> createAccount(@RequestBody AccountAddRequest account) {
        PostResponse response = accountService.createAccount(account);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("{accountId}")
    public ResponseEntity<PostResponse> updateAccount(@RequestBody AccountAddRequest edited,
                                                      @PathVariable Long accountId) {
        PostResponse response = accountService.updateAccount(edited, accountId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{accountId}")
    public ResponseEntity<PostResponse> deleteAccount(@PathVariable Long accountId) {
        PostResponse response = accountService.deleteAccount(accountId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
