package me.timickb.bookstore.api.service;


import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealService {
    private final DealRepository dealRepo;
    private final AccountRepository accountRepo;
    private final BookRepository bookRepo;
    private final LoggingService logger;

    @Autowired
    public DealService(DealRepository dealRepo, AccountRepository accountRepo, BookRepository bookRepo,
                       LoggingService logger) {
        this.dealRepo = dealRepo;
        this.accountRepo = accountRepo;
        this.bookRepo = bookRepo;
        this.logger = logger;
    }

    public List<Deal> getAllDeals() {
        return dealRepo.findAll();
    }

    public Optional<Deal> getDealById(long id) {
        return dealRepo.findById(id);
    }

    public List<Deal> getAllForAccount(long accountId) {
        return dealRepo.findAll().stream()
                .filter(d -> d.getAccount().getId() == accountId)
                .collect(Collectors.toList());
    }

    public List<Deal> getAllForBook(long bookId) {
        return dealRepo.findAll().stream()
                .filter(d -> d.getBook().getId() == bookId)
                .collect(Collectors.toList());
    }

    public PostResponse makeDeal(DealRequest request) {
        PostResponse response = new PostResponse();
        Optional<Book> bookOptional = bookRepo.findById(request.getBookId());
        Optional<Account> accountOptional = accountRepo.findById(request.getAccountId());

        if (bookOptional.isEmpty()) {
            response.setMessage("Book with id %d doesn't exist".formatted(request.getBookId()));
            return response;
        }

        if (accountOptional.isEmpty()) {
            response.setMessage("Account with id %d doesn't exist".formatted(request.getAccountId()));
            return response;
        }

        Book wantedBook = bookOptional.get();
        Account buyer = accountOptional.get();

        if (request.getAmount() > wantedBook.getAmount()) {
            response.setMessage("Not enough books left :(");
            return response;
        }

        if (wantedBook.getPrice() * request.getAmount() > buyer.getBalance()) {
            response.setMessage("This account doesn't have enough money :(");
            return response;
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

        logger.info("Account with id %d bought a book with id %d"
                .formatted(request.getAccountId(), request.getBookId()));

        response.setMessage("Deal succeeded!");
        response.setSucceeded(true);
        return response;
    }
}
