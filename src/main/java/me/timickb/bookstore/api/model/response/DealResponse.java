package me.timickb.bookstore.api.model.response;

import lombok.Data;

@Data
public class DealResponse {
    private String message;
    private boolean succeeded = false;
}
