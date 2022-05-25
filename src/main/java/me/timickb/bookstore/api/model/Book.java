package me.timickb.bookstore.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Book extends EntityBase {
    public String author;
    public String name;
    public int price;
    public int amount;
}
