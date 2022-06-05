package me.timickb.bookstore.api.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookInAccountResponse implements Serializable {
    private String author;
    private String name;
}
