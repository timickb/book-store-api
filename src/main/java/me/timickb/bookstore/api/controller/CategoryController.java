package me.timickb.bookstore.api.controller;

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
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<BookCategory>> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<BookCategory> getCategory(@PathVariable Long categoryId) {
        Optional<BookCategory> response = categoryService.getById(categoryId);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PostMapping
    public ResponseEntity<PostResponse> createCategory(@RequestBody BookCategory category) {
        PostResponse response = categoryService.createCategory(category);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<PostResponse> updateCategory(@RequestBody BookCategory edited,
                                                      @PathVariable Long categoryId) {
        PostResponse response = categoryService.updateCategory(edited, categoryId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<PostResponse> deleteCategory(@PathVariable Long categoryId) {
        PostResponse response = categoryService.deleteCategory(categoryId);
        if (response.isSucceeded()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
