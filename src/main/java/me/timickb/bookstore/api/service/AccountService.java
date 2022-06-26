package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.request.AccountAddRequest;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.PostResponse;

import java.util.List;
import java.util.Optional;

public interface AccountService extends EntityService<AccountResponse, AccountAddRequest> {
    List<AccountResponse> getAll();
    List<AccountResponse> getPageable(int page, int limit);
    Optional<AccountResponse> getById(long id);
    PostResponse create(final AccountAddRequest request);
    PostResponse update(final AccountAddRequest edited, long id);
    PostResponse delete(long id);
}
