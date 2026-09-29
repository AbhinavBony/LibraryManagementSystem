package com.springbootapplication.librarymanagementsystem.interfaces;

import com.springbootapplication.librarymanagementsystem.dto.BookRequestDto;
import com.springbootapplication.librarymanagementsystem.dto.BookResponseDto;

import java.util.List;

public interface BookService {
    List<BookResponseDto> getAllBooks();
    BookResponseDto addBookDetails(BookRequestDto bookDto);
    BookResponseDto updateBookDetails(Integer bookId, BookRequestDto bookDto);
    void deleteBook(Integer bookId);
    BookResponseDto getBookById(Integer bookId);
    List<BookResponseDto> getBookByAuthor(String authorName);
    List<BookResponseDto> getBookByGenre(String genreName);
}
