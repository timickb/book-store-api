package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.request.BookAddRequest;
import me.timickb.bookstore.api.model.request.BookFilter;
import me.timickb.bookstore.api.model.response.PostResponse;

import java.util.List;
import java.util.Optional;

public interface BookService extends EntityService<Book, BookAddRequest> {
    List<Book> getAll();
    List<Book> getPageable(int page, int limit);
    List<Book> getBooksByFilter(final BookFilter filter);
    Optional<Book> getById(long id);
    PostResponse create(final BookAddRequest request);
    PostResponse update(final BookAddRequest request, long id);
    PostResponse delete(long id);
}
