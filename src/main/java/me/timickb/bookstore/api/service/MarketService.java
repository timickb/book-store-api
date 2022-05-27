package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketService {
    private final DealRepository dealRepo;
    private final AccountRepository accountRepo;
    private final BookRepository bookRepo;

    @Autowired
    public MarketService(DealRepository dealRepo, AccountRepository accountRepo, BookRepository bookRepo) {
        this.dealRepo = dealRepo;
        this.accountRepo = accountRepo;
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll().stream()
                .filter(b -> b.getAmount() > 0).collect(Collectors.toList());
    }

    public Optional<Book> getBookById(long id) {
        return bookRepo.findById(id);
    }

    public boolean makeDeal(DealRequest request) {
        Optional<Book> bookOptional = getBookById(request.getBookId());
        Optional<Account> accountOptional = accountRepo.findById(request.getAccountId());

        if (bookOptional.isEmpty()) {
            return false;
        }

        if (accountOptional.isEmpty()) {
            return false;
        }

        Book wantedBook = bookOptional.get();
        Account buyer = accountOptional.get();

        if (request.getAmount() > wantedBook.getAmount()) {
            return false;
        }

        if (wantedBook.getPrice() * request.getAmount() > buyer.getBalance()) {
            return false;
        }

        List<Deal> existingDeals = dealRepo.findAll().stream()
                .filter(d -> d.getBook().getId() == request.getBookId() &&
                        d.getAccount().getId() == request.getAccountId())
                        .collect(Collectors.toList());

        // Проводим транзакцию
        wantedBook.setAmount(wantedBook.getAmount() - request.getAmount());
        buyer.setBalance(buyer.getBalance() - wantedBook.getPrice() * request.getAmount());
        bookRepo.saveAndFlush(wantedBook);
        accountRepo.saveAndFlush(buyer);

        Deal deal;
        if (existingDeals.isEmpty()) {
            // Создание новой сделки.
            deal = new Deal();
            deal.setAccount(buyer);
            deal.setBook(wantedBook);
            deal.setAmount(request.getAmount());
        } else {
            // Редактирование существующей.
            deal = existingDeals.get(0);
            deal.setAmount(deal.getAmount() + request.getAmount());
        }
        dealRepo.saveAndFlush(deal);

        return true;
    }
}