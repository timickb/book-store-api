package me.timickb.bookstore.api.mapper;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.response.AccountResponse;
import me.timickb.bookstore.api.service.BookService;
import me.timickb.bookstore.api.service.CategoryService;
import me.timickb.bookstore.api.service.DealService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = DealService.class)
public abstract class AccountMapper {

    @Autowired
    protected DealService dealService;

    @Mappings({
            @Mapping(target = "email", source = "account.email"),
            @Mapping(target = "balance", source = "account.balance"),
            @Mapping(target = "books", expression = "java(dealService.getPurchasesForAccount(account.getId()))")
    })
    public abstract AccountResponse accountToResponse(Account account);
}
