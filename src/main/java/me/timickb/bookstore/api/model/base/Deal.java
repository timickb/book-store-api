package me.timickb.bookstore.api.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Deal extends EntityBase {
    @ManyToOne
    private Account account;

    @ManyToOne
    public Book book;

    public int amount;


}
