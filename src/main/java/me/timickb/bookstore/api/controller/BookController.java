package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.Application;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.BookFilter;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController implements CrudController<Book, BookAddRequest> {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody BookAddRequest book) {
        PostResponse response = bookService.create(book);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping
    public ResponseEntity<List<Book>> readList(@RequestParam("page") Optional<Integer> page,
                                               @RequestParam("limit") Optional<Integer> limit) {
        if (page.isEmpty()) page = Optional.of(Application.DEFAULT_PAGE);
        if (limit.isEmpty()) limit = Optional.of(Application.DEFAULT_PAGE_LIMIT);
        return ResponseEntity.ok(bookService.getPageable(page.get(), limit.get()));
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> readOne(@PathVariable Long bookId) {
        Optional<Book> book = bookService.getById(bookId);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @PostMapping("filter")
    public ResponseEntity<List<Book>> getByFilter(@RequestBody BookFilter filter) {
        return ResponseEntity.ok(bookService.getBooksByFilter(filter));
    }

    @PutMapping("{bookId}")
    public ResponseEntity<PostResponse> update(@RequestBody BookAddRequest edited, @PathVariable Long bookId) {
        PostResponse response = bookService.update(edited, bookId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long bookId) {
        PostResponse response = bookService.delete(bookId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
