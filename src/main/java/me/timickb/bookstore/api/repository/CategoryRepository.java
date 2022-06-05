package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<BookCategory, Long> {
}
