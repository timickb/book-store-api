package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createBook(@RequestBody Book book) {
        PostResponse response = bookService.createBook(book);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable Long bookId) {
        Optional<Book> book = bookService.getBookById(bookId);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @PutMapping("{bookId}")
    public ResponseEntity<PostResponse> updateBook(@RequestBody Book edited, @PathVariable Long bookId) {
        PostResponse response = bookService.editBook(edited, bookId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<PostResponse> deleteBook(@PathVariable Long bookId) {
        PostResponse response = bookService.deleteBook(bookId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
