package com.springbootapplication.librarymanagementsystem.repository;

import com.springbootapplication.librarymanagementsystem.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByAuthorContainingIgnoreCase(String authorName);
    Optional<Book> findByTitle(String title);
    List<Book> findByGenreContainingIgnoreCase(String genreName);
}
