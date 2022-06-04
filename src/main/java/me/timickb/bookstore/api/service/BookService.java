package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.BookFilter;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class BookService {
    private final BookRepository bookRepo;
    private final CategoryRepository categoryRepo;
    private final LoggingService logger;
    private final ValidationService validator;

    @Autowired
    public BookService(BookRepository bookRepo, CategoryRepository categoryRepo, LoggingService logger, ValidationService validator) {
        this.bookRepo = bookRepo;
        this.categoryRepo = categoryRepo;
        this.logger = logger;
        this.validator = validator;
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll().stream()
                .filter(b -> b.getAmount() > 0).collect(Collectors.toList());
    }

    public List<Book> getBooksByFilter(BookFilter filter) {
        String checkResult = validator.validateBookFilter(filter);

        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            return new ArrayList<>();
        }

        List<Book> result = bookRepo.findAll().stream()
                .filter(b -> b.getName().contains(filter.getSearch()))
                .collect(Collectors.toList());

        switch (filter.getSortPrice()) {
            case "ASC" -> result.sort(Comparator.comparing(Book::getPrice));
            case "DESC" -> result.sort(Comparator.comparing(Book::getPrice).reversed());
        }

        return result;
    }

    public Optional<Book> getBookById(long id) {
        return bookRepo.findById(id);
    }

    public PostResponse createBook(BookAddRequest request) {
        PostResponse response = new PostResponse();
        Optional<BookCategory> category = categoryRepo.findById(request.getCategoryId());

        if (category.isEmpty()) {
            response.setMessage("Category with id %d doesn't exist.".formatted(request.getCategoryId()));
            return response;
        }

        Book book = new Book();
        book.setCategory(category.get());
        copyProperties(request, book);

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
