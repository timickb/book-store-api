package me.timickb.bookstore.api.model.request;

import lombok.Data;

@Data
public class DealRequest {
    private long bookId;
    private long accountId;
    private int amount;
}
