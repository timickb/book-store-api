package me.timickb.bookstore.api.model.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Account extends EntityBase {
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String passwordHash;
    private int balance;
}
