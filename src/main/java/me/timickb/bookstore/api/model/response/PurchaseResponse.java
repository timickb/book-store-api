package me.timickb.bookstore.api.model.response;

import lombok.Data;

@Data
public class PurchaseResponse {
    private BookResponse book;
    private int amount;
}
