package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.Application;
import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController implements CrudController<BookCategory, BookCategory> {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<BookCategory>> readList(@RequestParam Optional<Integer> page,
                                                       @RequestParam Optional<Integer> limit) {
        if (page.isEmpty()) page = Optional.of(Application.DEFAULT_PAGE);
        if (limit.isEmpty()) limit = Optional.of(Application.DEFAULT_PAGE_LIMIT);
        return ResponseEntity.ok(categoryService.getPageable(page.get(), limit.get()));
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<BookCategory> readOne(@PathVariable Long categoryId) {
        Optional<BookCategory> response = categoryService.getById(categoryId);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody BookCategory category) {
        PostResponse response = categoryService.create(category);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<PostResponse> update(@RequestBody BookCategory edited,
                                                      @PathVariable Long categoryId) {
        PostResponse response = categoryService.update(edited, categoryId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long categoryId) {
        PostResponse response = categoryService.delete(categoryId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
