package me.timickb.bookstore.api.model.request;

import lombok.Data;

@Data
public class AccountAddRequest {
    private String email;
    private String password;
    private int balance;
}
