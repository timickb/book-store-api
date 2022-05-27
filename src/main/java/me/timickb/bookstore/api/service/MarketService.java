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

    public PostResponse createBook(Book book) {
        PostResponse response = new PostResponse();
        if (book.getPrice() <= 0) {
            response.setMessage("Price should be positive");
            return response;
        }
        if (book.getAmount() < 0) {
            response.setMessage("Amount should be non-negative");
            return response;
        }
        if (book.getAuthor().isEmpty()) {
            response.setMessage("Author name should be non-empty");
            return response;
        }
        if (book.getName().isEmpty()) {
            response.setMessage("Book name should be non-empty");
            return response;
        }
        bookRepo.saveAndFlush(book);
        response.setSucceeded(true);
        response.setMessage("Book added!");
        return response;
    }

    public PostResponse makeDeal(DealRequest request) {
        PostResponse response = new PostResponse();
        Optional<Book> bookOptional = getBookById(request.getBookId());
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

        response.setMessage("Deal succeeded!");
        response.setSucceeded(true);
        return response;
    }
}
