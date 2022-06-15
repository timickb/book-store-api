package me.timickb.bookstore.api.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Account extends EntityBase {
    private String email;
    private String passwordHash;
    private int balance;
}
