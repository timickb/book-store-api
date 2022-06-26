package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.response.PostResponse;

import java.util.List;
import java.util.Optional;

public interface EntityService<T, U> {
    List<T> getAll();
    List<T> getPageable(int page, int limit);
    Optional<T> getById(long id);
    PostResponse create(final U request);
    PostResponse update(final U request, long id);
    PostResponse delete(long id);
}