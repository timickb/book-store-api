package me.timickb.bookstore.api.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AccountResponse implements Serializable {
    private String email;
    private int balance;
    private List<PurchaseResponse> books;
}
