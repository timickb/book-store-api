package me.timickb.bookstore.api.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookResponse implements Serializable {
    private String author;
    private String name;
}
