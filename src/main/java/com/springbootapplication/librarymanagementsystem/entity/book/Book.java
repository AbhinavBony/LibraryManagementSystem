package com.springbootapplication.librarymanagementsystem.entity.book;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Integer Id;
    @Column(name = "book_title", nullable = false)
    private String title;
    @Column(name = "book_author", nullable = false)
    private String author;
    @Column(name = "book_genre")
    private String genre;
    @Column(name = "book_price", nullable = false)
    private Double price;
}
