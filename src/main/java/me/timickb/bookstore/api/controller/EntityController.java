package me.timickb.bookstore.api.controller;

import me.timickb.bookstore.api.model.response.PostResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EntityController<T, U> {
    ResponseEntity<List<T>> getList(Optional<Integer> page, Optional<Integer> limit);
    ResponseEntity<T> getOne(Long id);
    ResponseEntity<PostResponse> create(U request);
    ResponseEntity<PostResponse> update(U request, Long id);
    ResponseEntity<PostResponse> delete(Long id);
}
