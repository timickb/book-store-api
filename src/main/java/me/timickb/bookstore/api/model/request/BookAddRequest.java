package me.timickb.bookstore.api.model.request;

import lombok.Data;

@Data
public class BookAddRequest {
    private String author;
    private String name;
    private int price;
    private int amount;
    private long categoryId;
}
