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
    private final LoggingService logger;
    private final ValidationService validator;

    @Autowired
    public MarketService(DealRepository dealRepo, AccountRepository accountRepo, BookRepository bookRepo,
                         LoggingService logger, ValidationService validator) {
        this.dealRepo = dealRepo;
        this.accountRepo = accountRepo;
        this.bookRepo = bookRepo;
        this.logger = logger;
        this.validator = validator;
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
        String checkResult = validator.validateBook(book);

        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            response.setMessage(checkResult);
            return response;
        }

        bookRepo.saveAndFlush(book);

        logger.info("Created book with id %d".formatted(book.getId()));
        response.setSucceeded(true);
        response.setMessage("Book added!");
        return response;
    }

    public PostResponse editBook(Book edited, long id) {
        PostResponse response = new PostResponse();
        Optional<Book> existing = bookRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Book with id %d doesn't exist".formatted(id));
            return response;
        }

        String checkResult = validator.validateBook(edited);
        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            response.setMessage(checkResult);
            return response;
        }

        Book book = existing.get();
        book.setPrice(edited.getPrice());
        book.setAuthor(edited.getAuthor());
        book.setName(edited.getName());
        book.setAmount(edited.getAmount());
        bookRepo.saveAndFlush(book);

        logger.info("Updated book with id %d".formatted(id));

        response.setMessage("Book updated!");
        response.setSucceeded(true);
        return response;
    }

    public PostResponse deleteBook(long id) {
        PostResponse response = new PostResponse();
        Optional<Book> existing = bookRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Book with id %d doesn't exist".formatted(id));
            return response;
        }

        bookRepo.delete(existing.get());

        logger.info("Deleted book with id %d".formatted(id));

        response.setMessage("Book deleted");
        response.setSucceeded(true);
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

        logger.info("Account with id %d bought a book with id %d"
                .formatted(request.getAccountId(), request.getBookId()));

        response.setMessage("Deal succeeded!");
        response.setSucceeded(true);
        return response;
    }
}
