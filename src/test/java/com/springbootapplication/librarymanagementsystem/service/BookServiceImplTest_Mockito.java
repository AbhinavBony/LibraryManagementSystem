package com.springbootapplication.librarymanagementsystem.service;

import com.springbootapplication.librarymanagementsystem.dto.BookRequestDto;
import com.springbootapplication.librarymanagementsystem.dto.BookResponseDto;
import com.springbootapplication.librarymanagementsystem.entity.book.Book;
import com.springbootapplication.librarymanagementsystem.exception.BookNotFound;
import com.springbootapplication.librarymanagementsystem.exception.DuplicateBookFound;
import com.springbootapplication.librarymanagementsystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class bookServiceImplTestMockito {
    @Mock
    private BookRepository repo;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private BookServiceImpl service;

    Book book;
    BookRequestDto requestDto;
    BookResponseDto responseDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");
        book.setGenre("Fantasy");
        book.setPrice(3500.87);

        requestDto = new BookRequestDto();
        requestDto.setTitle("Harry Potter");
        requestDto.setAuthor("J.K. Rowling");
        requestDto.setGenre("Fantasy");
        requestDto.setPrice(3500.87);

        responseDto = new BookResponseDto();
        responseDto.setId(1);
        responseDto.setTitle("Harry Potter");
        responseDto.setAuthor("J.K. Rowling");
        responseDto.setGenre("Fantasy");
        responseDto.setPrice(3500.87);
    }
    @Test
    void getAllBooks(){
        when(repo.findAll()).thenReturn(List.of(book));
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        List<BookResponseDto> list = this.service.getAllBooks();
        assertEquals(1, list.size());
        assertEquals(responseDto.getTitle(), list.getFirst().getTitle());
        verify(repo, times(1)).findAll();
    }
    @Test
    void getAllBooks_whenListIEmpty(){

        List<BookResponseDto> list = this.service.getAllBooks();

        assertTrue(list.isEmpty());
    }
    @Test
    void addBook_shouldSaveAndReturn(){
        when(repo.findByTitle(requestDto.getTitle())).thenReturn(Optional.empty());
        when(mapper.map(requestDto, Book.class)).thenReturn(book);
        when(repo.save(book)).thenReturn(book);
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        BookResponseDto saved = this.service.addBookDetails(requestDto);

        assertNotNull(saved);
        assertEquals(responseDto.getTitle(), saved.getTitle());
        assertEquals(responseDto.getId(), saved.getId());
        verify(repo, times(1)).save(book);
    }
    @Test
    void addBook_shouldThrowException_whenDuplicateBookFound(){
        when(repo.findByTitle(requestDto.getTitle())).thenReturn(Optional.of(book));

        assertThrows(DuplicateBookFound.class, ()-> this.service.addBookDetails(requestDto));
        verify(repo, never()).save(any());
    }
    @Test
    void deleteBook_shouldDelete_whenFound(){
        when(repo.existsById(book.getId())).thenReturn(true);
        this.service.deleteBook(book.getId());

        verify(repo, times(1)).existsById(book.getId());
        verify(repo, times(1)).deleteById(book.getId());
    }
    @Test
    void deleteBook_shouldThrowException_whenNotFound(){
        when(repo.existsById(999)).thenReturn(false);

        assertThrows(BookNotFound.class, ()-> this.service.deleteBook(999));
        verify(repo, never()).deleteById(anyInt());
    }
    @Test
    void getListOfBooks_byAuthor(){
        when(repo.findByAuthorContainingIgnoreCase(requestDto.getAuthor())).thenReturn(List.of(book));
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        List<BookResponseDto> list = this.service.getBookByAuthor(requestDto.getAuthor());

        assertEquals(1, list.size());
        assertEquals(responseDto.getTitle(), list.getFirst().getTitle());
        assertEquals(responseDto.getAuthor(), list.getFirst().getAuthor());
        verify(repo, times(1)).findByAuthorContainingIgnoreCase(requestDto.getAuthor());
    }
    @Test
    void getListOfBooks_byAuthor_whenNotFound(){

        List<BookResponseDto> list = this.service.getBookByAuthor("Chetan Bhagat");
        assertTrue(list.isEmpty());
    }
    @Test
    void getListOfBooks_byGenre(){
        when(repo.findByGenreContainingIgnoreCase(requestDto.getGenre())).thenReturn(List.of(book));
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        List<BookResponseDto> list = this.service.getBookByGenre(requestDto.getGenre());

        assertEquals(1, list.size());
        assertEquals(responseDto.getTitle(), list.getFirst().getTitle());
        assertEquals(responseDto.getGenre(), list.getFirst().getGenre());
        verify(repo, times(1)).findByGenreContainingIgnoreCase(requestDto.getGenre());
    }
    @Test
    void getListOfBooks_byGenre_whenNotFound(){
        List<BookResponseDto> list = this.service.getBookByGenre("Thriller");

        assertTrue(list.isEmpty());
    }
    @Test
    void updateBook_updateAllFields_whenFound(){
        BookRequestDto updateDto = new BookRequestDto();
        updateDto.setTitle("Harry Potter And The Chamber Of Secrets");
        updateDto.setAuthor("Jonne Keith Rowling");
        updateDto.setGenre("Fantasy (Magic)");
        updateDto.setPrice(4589.78);

        when(repo.findById(book.getId())).thenReturn(Optional.of(book));
        when(repo.save(book)).thenReturn(book);
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        this.service.updateBookDetails(book.getId(), updateDto);

        assertEquals(updateDto.getTitle(), book.getTitle());
        assertEquals(updateDto.getAuthor(), book.getAuthor());
        verify(repo, times(1)).save(book);
    }
    @Test
    void updateBook_updateOnlyTitle_whenFound(){
        BookRequestDto updateDto = new BookRequestDto();
        updateDto.setTitle("Harry Potter And The Chamber Of Secrets");

        when(repo.findById(book.getId())).thenReturn(Optional.of(book));
        when(repo.save(book)).thenReturn(book);
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        this.service.updateBookDetails(book.getId(), updateDto);

        assertEquals(updateDto.getTitle(), book.getTitle());
        assertEquals(responseDto.getAuthor(), book.getAuthor());
        assertEquals(responseDto.getGenre(), book.getGenre());
        verify(repo, times(1)).save(book);
    }
    @Test
    void updateBook_shouldThrowException_whenNotFound(){
        when(repo.findById(999)).thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, ()-> this.service.updateBookDetails(999, requestDto));
    }
    @Test
    void getBookById_shouldReturn_whenFound(){
        when(repo.findById(book.getId())).thenReturn(Optional.of(book));
        when(mapper.map(book, BookResponseDto.class)).thenReturn(responseDto);

        BookResponseDto found = this.service.getBookById(book.getId());

        assertNotNull(found);
        assertEquals(responseDto.getTitle(), found.getTitle());
        verify(repo, times(1)).findById(book.getId());
    }
    @Test
    void getBookById_shouldThrowException_whenNotFound(){
        when(repo.findById(999)).thenReturn(Optional.empty());

        assertThrows(BookNotFound.class, ()-> this.service.getBookById(999));
    }
}