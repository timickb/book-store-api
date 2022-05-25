package me.timickb.bookstore.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Account extends EntityBase {
    public int balance;
}
