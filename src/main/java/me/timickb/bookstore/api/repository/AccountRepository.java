package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByBalance(int balance);
}
