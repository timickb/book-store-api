package me.timickb.bookstore.api.model.response;

import lombok.Data;

@Data
public class PostResponse {
    private String message;
    private boolean succeeded = false;
}
