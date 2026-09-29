package com.springbootapplication.librarymanagementsystem.service;

import com.springbootapplication.librarymanagementsystem.dto.BookRequestDto;
import com.springbootapplication.librarymanagementsystem.dto.BookResponseDto;
import com.springbootapplication.librarymanagementsystem.entity.book.Book;
import com.springbootapplication.librarymanagementsystem.exception.BookNotFound;
import com.springbootapplication.librarymanagementsystem.exception.DuplicateBookFound;
import com.springbootapplication.librarymanagementsystem.interfaces.BookService;
import com.springbootapplication.librarymanagementsystem.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    private final ModelMapper mapper;
    private final BookRepository repo;

    public BookServiceImpl(ModelMapper mapper, BookRepository repo) {
        this.mapper = mapper;
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getAllBooks() {
        return repo.findAll().stream().map(book-> mapper.map(book, BookResponseDto.class)).toList();
    }

    @Override
    @Transactional
    public BookResponseDto addBookDetails(BookRequestDto bookDto) {
        repo.findByTitle(bookDto.getTitle()).ifPresent((book)-> {
            throw new DuplicateBookFound("Book already exists with title: "+ bookDto.getTitle());
        });
        Book book = mapper.map(bookDto, Book.class);
        book.setTitle(bookDto.getTitle().trim());
        book.setAuthor(bookDto.getAuthor().trim());
        book.setGenre(bookDto.getGenre().trim());
        Book saved = repo.save(book);
        return this.mapper.map(saved, BookResponseDto.class);
    }

    @Override
    @Transactional
    public BookResponseDto updateBookDetails(Integer bookId, BookRequestDto bookDto) {
        Book found = repo.findById(bookId).orElseThrow(()-> new BookNotFound("Book not found with Book Id: "+ bookId));
        if (bookDto.getTitle() != null) {
            found.setTitle(bookDto.getTitle().trim());
        }
        if (bookDto.getAuthor() != null) {
            found.setAuthor(bookDto.getAuthor().trim());
        }
        if (bookDto.getGenre() != null) {
            found.setGenre(bookDto.getGenre().trim());
        }
        if (bookDto.getPrice() != null) {
            found.setPrice(bookDto.getPrice());
        }
        Book updated = repo.save(found);
        return this.mapper.map(updated, BookResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteBook(Integer bookId) {
        if (!repo.existsById(bookId)){
            throw new BookNotFound("Book not found with Book Id: "+ bookId);
        }
        repo.deleteById(bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto getBookById(Integer bookId) {
        Book found = repo.findById(bookId).orElseThrow(()-> new BookNotFound("Book not found with Book Id: "+ bookId));
        return this.mapper.map(found, BookResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBookByAuthor(String authorName) {
        return repo.findByAuthorContainingIgnoreCase(authorName).stream().map(bookByAuthor-> this.mapper.map(bookByAuthor, BookResponseDto.class)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBookByGenre(String genreName) {
        return repo.findByGenreContainingIgnoreCase(genreName).stream().map(bookByGenre-> this.mapper.map(bookByGenre, BookResponseDto.class)).toList();
    }
}