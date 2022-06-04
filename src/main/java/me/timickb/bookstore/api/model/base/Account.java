package me.timickb.bookstore.api.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Table(name = "accounts")
@Entity
@Data
public class Account extends EntityBase {
    private String email;
    private String passwordHash;
    private int balance;
}
