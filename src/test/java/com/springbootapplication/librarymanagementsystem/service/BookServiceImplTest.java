package com.springbootapplication.librarymanagementsystem.service;

import com.springbootapplication.librarymanagementsystem.dto.BookRequestDto;
import com.springbootapplication.librarymanagementsystem.dto.BookResponseDto;
import com.springbootapplication.librarymanagementsystem.exception.BookNotFound;
import com.springbootapplication.librarymanagementsystem.exception.DuplicateBookFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class BookServiceImplTest {
    @Autowired
    private BookServiceImpl service;
    BookRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new BookRequestDto();
        requestDto.setTitle("Harry Potter");
        requestDto.setAuthor("J.K. Rowling");
        requestDto.setGenre("Fantasy");
        requestDto.setPrice(3500.87);
    }
    @Test
    void getListOfBooks(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        List<BookResponseDto> list = this.service.getAllBooks();

        assertEquals(1, list.size());
        assertEquals(saved.getTitle(), list.getFirst().getTitle());
        assertEquals(saved.getId(), list.getFirst().getId());
    }
    @Test
    void getAllBooks_whenListIsEmpty(){
        List<BookResponseDto> list = this.service.getAllBooks();

        assertTrue(list.isEmpty());
    }
    @Test
    void addBook_shouldSaveAndReturn(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);

        assertNotNull(saved);
        assertEquals(requestDto.getTitle(), saved.getTitle());
        assertEquals(requestDto.getPrice(), saved.getPrice());
    }
    @Test
    void addBook_shouldThrowException_whenDuplicateBookFound(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        assertThrows(DuplicateBookFound.class, ()-> this.service.addBookDetails(requestDto));
    }
    @Test
    void updateBook_shouldUpdate_whenFoundByBookId(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        BookRequestDto updateBook = new BookRequestDto();
        updateBook.setTitle("Harry Potter And The Chamber Of Secrets!!");
        updateBook.setAuthor("Jonne Rowling");
        updateBook.setGenre("Fantasy (Magic)");
        updateBook.setPrice(4000.12);
        BookResponseDto updatedDetails = this.service.updateBookDetails(saved.getId(), updateBook);

        assertNotNull(updatedDetails);
        assertEquals(updateBook.getTitle().trim(), updatedDetails.getTitle());
        assertEquals(updateBook.getAuthor().trim(), updatedDetails.getAuthor());
    }
    @Test
    void updateBook_shouldThrowException_whenNotFoundBook(){
        assertThrows(BookNotFound.class, ()-> this.service.updateBookDetails(999, requestDto));
    }
    @Test
    void deleteBook_shouldDelete_whenFound(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        this.service.deleteBook(saved.getId());

        assertThrows(BookNotFound.class, ()-> service.getBookById(saved.getId()));
    }
    @Test
    void deleteBook_shouldThrowException_whenNotFound(){

        assertThrows(BookNotFound.class, ()-> this.service.deleteBook(9999));
    }
    @Test
    void getBookById_shouldReturnBook_whenFound(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        BookResponseDto found = this.service.getBookById(saved.getId());

        assertEquals(saved.getTitle(), found.getTitle());
    }
    @Test
    void getBookById_shouldThrowException_whenNotFound(){

        assertThrows(BookNotFound.class, ()-> this.service.getBookById(9999));
    }
     @Test
    void searchBookByAuthor_whenFound(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        List<BookResponseDto> listByAuthor = this.service.getBookByAuthor(requestDto.getAuthor());

        assertEquals(1, listByAuthor.size());
        assertEquals(saved.getTitle(), listByAuthor.getFirst().getTitle());
        assertEquals(saved.getAuthor(), listByAuthor.getFirst().getAuthor());
    }
    @Test
    void searchBookByAuthor_whenAuthorNotFound(){
        List<BookResponseDto> listByAuthor = this.service.getBookByAuthor("Chetan Bhagat");
        assertTrue(listByAuthor.isEmpty());
    }
    @Test
    void searchBookByGenre_whenFound(){
        BookResponseDto saved = this.service.addBookDetails(requestDto);
        List<BookResponseDto> listByGenre = this.service.getBookByGenre(requestDto.getGenre());

        assertEquals(saved.getGenre(), listByGenre.getFirst().getGenre());
        assertEquals(saved.getTitle(), listByGenre.getFirst().getTitle());
    }
    @Test
    void searchBookByGenre_shouldThrowException_whenNotFound(){
        List<BookResponseDto> listByGenre = this.service.getBookByGenre("Thriller");
        assertTrue(listByGenre.isEmpty());
    }
}