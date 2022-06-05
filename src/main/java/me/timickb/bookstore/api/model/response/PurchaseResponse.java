package me.timickb.bookstore.api.model.response;

import lombok.Data;

@Data
public class PurchaseResponse {
    private BookInAccountResponse book;
    private int amount;
}
