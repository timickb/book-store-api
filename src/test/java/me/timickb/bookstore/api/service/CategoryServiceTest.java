package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @MockBean
    private CategoryRepository categoryRepo;

    @Test
    public void getAllCategories() {
        List<BookCategory> categories = categoryService.getAllCategories();
        Assertions.assertNotNull(categories);
        Mockito.verify(categoryRepo, Mockito.times(1)).findAll();
    }

    @Test
    public void getById() {
        BookCategory existing = new BookCategory();
        existing.setId(1);

        Mockito.doReturn(Optional.of(existing)).when(categoryRepo).findById(1L);

        Optional<BookCategory> optional = categoryService.getById(1);
        Assertions.assertTrue(optional.isPresent());
    }

    @Test
    public void createCategory() {
        BookCategory category = new BookCategory();
        category.setName("test");
        PostResponse response = categoryService.createCategory(category);

        Assertions.assertTrue(response.isSucceeded());
        Mockito.verify(categoryRepo, Mockito.times(1))
                .saveAndFlush(category);
    }

    @Test
    public void deleteCategory() {
        BookCategory existing = new BookCategory();
        existing.setId(1);

        Mockito.doReturn(Optional.of(existing)).when(categoryRepo).findById(1L);

        PostResponse response = categoryService.deleteCategory(1);
        Assertions.assertTrue(response.isSucceeded());
        Mockito.verify(categoryRepo, Mockito.times(1)).delete(existing);
    }
}