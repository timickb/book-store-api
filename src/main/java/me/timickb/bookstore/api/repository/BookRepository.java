package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
