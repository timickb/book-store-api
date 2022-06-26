package me.timickb.bookstore.api.service.implementation;

import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.CategoryRepository;
import me.timickb.bookstore.api.service.CategoryService;
import me.timickb.bookstore.api.service.implementation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final ValidationService validationService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepo, ValidationService validationService) {
        this.categoryRepo = categoryRepo;
        this.validationService = validationService;
    }

    public List<BookCategory> getAll() {
        return categoryRepo.findAll();
    }

    public List<BookCategory> getPageable(int page, int limit) {
        return categoryRepo.findAll(PageRequest.of(page, limit)).toList();
    }

    public Optional<BookCategory> getById(long id) {
        return categoryRepo.findById(id);
    }

    public PostResponse create(final BookCategory category) {
        PostResponse response = new PostResponse();

        return getPostResponse("Category added", category, response);
    }

    public PostResponse update(final BookCategory category, long id) {
        PostResponse response = new PostResponse();
        Optional<BookCategory> existing = categoryRepo.findById(id);

        if (existing.isEmpty()) {
            response.setMessage("Category not found");
            return response;
        }

        return getPostResponse("Category updated", category, response);
    }

    public PostResponse delete(long id) {
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