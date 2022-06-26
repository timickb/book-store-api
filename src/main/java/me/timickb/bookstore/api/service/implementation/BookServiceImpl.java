package me.timickb.bookstore.api.service.implementation;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.BookFilter;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.BookRepository;
import me.timickb.bookstore.api.repository.CategoryRepository;
import me.timickb.bookstore.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;
    private final CategoryRepository categoryRepo;
    private final LoggingService logger;
    private final ValidationService validator;

    @Autowired
    public BookServiceImpl(BookRepository bookRepo, CategoryRepository categoryRepo, LoggingService logger, ValidationService validator) {
        this.bookRepo = bookRepo;
        this.categoryRepo = categoryRepo;
        this.logger = logger;
        this.validator = validator;
    }

    public List<Book> getAll() {
        return bookRepo.findByAmountGreaterThan(0);
    }

    public List<Book> getPageable(int page, int limit) {
        return bookRepo.findByAmountGreaterThan(0, PageRequest.of(page, limit))
                .toList();
    }

    public List<Book> getBooksByFilter(final BookFilter filter) {
        String checkResult = validator.validateBookFilter(filter);

        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            return Collections.emptyList();
        }

        List<Book> result = bookRepo.findByAmountGreaterThan(0);

        if (filter.getSearch() != null) {
            result = result.stream().filter(b -> b.getName().contains(filter.getSearch()))
                    .collect(Collectors.toList());
        }

        if (filter.getCategoryId() != null) {
            result = result.stream().filter(b -> b.getCategory().getId() == filter.getCategoryId())
                    .collect(Collectors.toList());
        }

        if (filter.getSortPrice() != null) {
            switch (filter.getSortPrice()) {
                case "ASC" -> result.sort(Comparator.comparing(Book::getPrice));
                case "DESC" -> result.sort(Comparator.comparing(Book::getPrice).reversed());
            }
        }

        return result;
    }

    public Optional<Book> getById(long id) {
        return bookRepo.findById(id);
    }

    public PostResponse create(final BookAddRequest request) {
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

    public PostResponse update(final BookAddRequest edited, long id) {
        PostResponse response = new PostResponse();
        Optional<Book> existing = bookRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Book with id %d doesn't exist".formatted(id));
            return response;
        }

        Optional<BookCategory> category = categoryRepo.findById(edited.getCategoryId());

        if (category.isEmpty()) {
            response.setMessage("Category with id %d doesn't exist.".formatted(edited.getCategoryId()));
            return response;
        }

        Book editedBase = new Book();
        editedBase.setCategory(category.get());
        copyProperties(edited, editedBase);

        String checkResult = validator.validateBook(editedBase);
        if (!checkResult.equals(ValidationService.SUCCESS_MSG)) {
            response.setMessage(checkResult);
            return response;
        }

        Book book = existing.get();
        book.setPrice(editedBase.getPrice());
        book.setAuthor(editedBase.getAuthor());
        book.setName(editedBase.getName());
        book.setAmount(editedBase.getAmount());
        book.setCoverUrl(editedBase.getCoverUrl());
        book.setCategory(category.get());
        bookRepo.saveAndFlush(book);

        logger.info("Updated book with id %d".formatted(id));

        response.setMessage("Book updated!");
        response.setSucceeded(true);
        return response;
    }

    public PostResponse delete(long id) {
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
