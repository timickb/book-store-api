package me.timickb.bookstore.api.mapper;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Book;
import me.timickb.bookstore.api.model.base.Deal;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.model.response.BookInAccountResponse;
import me.timickb.bookstore.api.model.response.PurchaseResponse;
import me.timickb.bookstore.api.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps base entities to response entities.
 */
@Component
public class ResponseMapper {
    private final DealRepository dealRepo;

    @Autowired
    public ResponseMapper(DealRepository dealRepo) {
        this.dealRepo = dealRepo;
    }

    /**
     * Maps Account entity to the AccountResponse object.
     * @param account Account entity
     * @return Mapped entity
     */
    public AccountResponse responseFromAccount(Account account) {
        List<Deal> deals = dealRepo.findAll().stream()
                .filter(d -> d.getAccount().getId() == account.getId())
                .findFirst().stream().toList();

        List<PurchaseResponse> accountBooks = new ArrayList<>();

        for(Deal deal : deals) {
            accountBooks.add(bookAndAmountToPurchase(deal.getBook(), deal.getAmount()));
        }

        AccountResponse response = new AccountResponse();
        response.setBooks(accountBooks);
        response.setBalance(account.getBalance());
        response.setEmail(account.getEmail());

        return response;
    }

    /**
     * Maps Book entity with data of amount of purchase
     * to the PurchaseResponse object.
     * @param book Book entity
     * @param amount Purchase amount
     * @return Mapped entity
     */
    public PurchaseResponse bookAndAmountToPurchase(Book book, int amount) {
        PurchaseResponse response = new PurchaseResponse();
        BookInAccountResponse book1 = new BookInAccountResponse();

        book1.setAuthor(book.getAuthor());
        book1.setName(book.getName());

        response.setBook(book1);
        response.setAmount(amount);

        return response;
    }
}
