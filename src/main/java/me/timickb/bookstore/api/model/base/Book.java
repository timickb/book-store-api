package me.timickb.bookstore.api.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Table(name = "books")
@Data
@Entity
public class Book extends EntityBase {
    private String author;
    private String name;
    private int price;
    private int amount;
}