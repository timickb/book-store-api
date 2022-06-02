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
public class BookService {
    private final BookRepository bookRepo;
    private final LoggingService logger;
    private final ValidationService validator;

    @Autowired
    public BookService(DealRepository dealRepo, AccountRepository accountRepo, BookRepository bookRepo,
                       LoggingService logger, ValidationService validator) {
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
}
