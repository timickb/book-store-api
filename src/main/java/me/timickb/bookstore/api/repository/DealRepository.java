package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
}
