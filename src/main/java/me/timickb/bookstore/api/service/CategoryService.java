package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.BookCategory;
import me.timickb.bookstore.api.model.response.PostResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends EntityService<BookCategory, BookCategory> {
    List<BookCategory> getAll();
    Optional<BookCategory> getById(long id);
    PostResponse create(final BookCategory category);
    PostResponse update(final BookCategory category, long id);
    PostResponse delete(long id);
}