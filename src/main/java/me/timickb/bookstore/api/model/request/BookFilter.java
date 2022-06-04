package me.timickb.bookstore.api.model.request;

import lombok.Data;

@Data
public class BookFilter {
    private String search;
    private String sortPrice;
    private Long categoryId;
}
