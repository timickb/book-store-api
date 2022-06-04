package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;
    private final ValidationService validationService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepo, ValidationService validationService) {
        this.categoryRepo = categoryRepo;
        this.validationService = validationService;
    }

    public List<BookCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Optional<BookCategory> getById(long id) {
        return categoryRepo.findById(id);
    }

    public PostResponse createCategory(BookCategory category) {
        PostResponse response = new PostResponse();

        return getPostResponse("Category added", category, response);
    }

    public PostResponse updateCategory(BookCategory category, long id) {
        PostResponse response = new PostResponse();
        Optional<BookCategory> existing = categoryRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Category not found");
            return response;
        }

        return getPostResponse("Category updated", category, response);
    }

    public PostResponse deleteCategory(long id) {
        PostResponse response = new PostResponse();
        Optional<BookCategory> existing = categoryRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Category not found");
            return response;
        }

        categoryRepo.delete(existing.get());

        response.setMessage("Category deleted");
        response.setSucceeded(true);
        return response;
    }

    private PostResponse getPostResponse(String msg, BookCategory category, PostResponse response) {
        String checkResponse = validationService.validateCategory(category);
        if (!checkResponse.equals(ValidationService.SUCCESS_MSG)) {
            response.setMessage(checkResponse);
            return response;
        }

        categoryRepo.saveAndFlush(category);
        response.setSucceeded(true);
        response.setMessage(msg);
        return response;
    }
}
