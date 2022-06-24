package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Account;
import me.timickb.bookstore.api.model.base.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findAllByAccount(Account account);
}
