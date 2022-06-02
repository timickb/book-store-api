package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
