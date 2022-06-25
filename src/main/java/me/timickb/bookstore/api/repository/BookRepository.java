package me.timickb.bookstore.api.repository;

import me.timickb.bookstore.api.model.base.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAmountGreaterThan(Integer minimum, Pageable pageable);
    List<Book> findByAmountGreaterThan(Integer minimum);
}
