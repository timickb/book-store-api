package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
