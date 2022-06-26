package me.timickb.bookstore.api.service.implementation;


import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.BookInAccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.model.response.PurchaseResponse;
import me.timickb.bookstore.api.repository.AccountRepository;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.DealRepository;
import me.timickb.bookstore.api.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepo;
    private final AccountRepository accountRepo;
    private final BookRepository bookRepo;
    private final LoggingService logger;

    @Autowired
    public DealServiceImpl(DealRepository dealRepo, AccountRepository accountRepo, BookRepository bookRepo, LoggingService logger) {
        this.dealRepo = dealRepo;
        this.accountRepo = accountRepo;
        this.bookRepo = bookRepo;
        this.logger = logger;
    }

    public PostResponse deleteDeal(long id) {
        PostResponse response = new PostResponse();
        Optional<Deal> existing = dealRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Deal with id %d doesn't exist".formatted(id));
            return response;
        }

        dealRepo.delete(existing.get());

        response.setSucceeded(true);
        response.setMessage("Deal removed");
        return response;
    }

    public List<Deal> getDealsPageable(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return dealRepo.findAll(pageable).toList();
    }

    public Optional<Deal> getDealById(long id) {
        return dealRepo.findById(id);
    }

    public List<Deal> getAllForAccount(long accountId) {
        return dealRepo.findAll().stream().filter(d -> d.getAccount().getId() == accountId).collect(Collectors.toList());
    }

    public List<PurchaseResponse> getPurchasesForAccount(long accountId) {
        Optional<Account> account = accountRepo.findById(accountId);

        if (account.isEmpty()) {
            return Collections.emptyList();
        }

        List<Deal> deals = dealRepo.findAllByAccount(account.get());

        List<PurchaseResponse> result = new ArrayList<>();

        for (Deal deal : deals) {
            BookInAccountResponse book = new BookInAccountResponse();
            PurchaseResponse item = new PurchaseResponse();
            book.setAuthor(deal.getBook().getAuthor());
            book.setName(deal.getBook().getName());
            item.setBook(book);
            item.setAmount(deal.getAmount());
            result.add(item);
        }

        return result;
    }

    public List<Deal> getAllForBook(long bookId) {
        return dealRepo.findAll().stream().filter(d -> d.getBook().getId() == bookId).collect(Collectors.toList());
    }

    public PostResponse makeDeal(final DealRequest request) {
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
                .filter(d -> d.getBook().getId() == request.getBookId() && d.getAccount().getId() == request.getAccountId())
                .toList();

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
        dealRepo.save(deal);

        logger.info("Account with id %d bought a book with id %d (%d items)".formatted(
                request.getAccountId(),
                request.getBookId(),
                request.getAmount()));

        response.setMessage("Deal succeeded!");
        response.setSucceeded(true);
        return response;
    }
}
