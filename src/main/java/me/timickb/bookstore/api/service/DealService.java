package me.timickb.bookstore.api.service;

import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.request.DealRequest;
import me.timickb.bookstore.api.model.response.PostResponse;
import me.timickb.bookstore.api.model.response.PurchaseResponse;

import java.util.List;
import java.util.Optional;

public interface DealService {
    List<Deal> getDealsPageable(int page, int limit);
    List<Deal> getAllForAccount(long accountId);
    List<Deal> getAllForBook(long bookId);
    List<PurchaseResponse> getPurchasesForAccount(long accountId);
    Optional<Deal> getDealById(long id);
    PostResponse makeDeal(final DealRequest request);
    PostResponse deleteDeal(long dealId);
}
