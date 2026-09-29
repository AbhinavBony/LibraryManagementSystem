package com.springbootapplication.librarymanagementsystem.controller;

import com.springbootapplication.librarymanagementsystem.dto.BookRequestDto;
import com.springbootapplication.librarymanagementsystem.dto.BookResponseDto;
import com.springbootapplication.librarymanagementsystem.interfaces.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
@Validated
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }
    @GetMapping(value = "/books")
    public ResponseEntity<List<BookResponseDto>> getAll(){
        List<BookResponseDto> bookList = this.service.getAllBooks();
        if (bookList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(bookList);
    }
    @PostMapping(value = "/books")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookResponseDto> addBook(@RequestBody @Valid BookRequestDto bookDto){
        BookResponseDto saved = this.service.addBookDetails(bookDto);
        return ResponseEntity.ok(saved);
    }
    @PatchMapping(value = "/books/{Id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable(name = "Id") @Positive(message = "Id should be positive") Integer bookId, @RequestBody BookRequestDto bookDto){
        BookResponseDto updated = this.service.updateBookDetails(bookId, bookDto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping(value = "/books/{Id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> deleteBook(@PathVariable(name = "Id") @Positive(message = "Id should be positive") Integer bookId){
        this.service.deleteBook(bookId);
        return ResponseEntity.ok("Book details deleted successfully!!");
    }
    @GetMapping(value = "/books/{Id}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable(name = "Id") @Positive(message = "Id should be positive") Integer bookId){
        BookResponseDto found = this.service.getBookById(bookId);
        return ResponseEntity.ok(found);
    }
    @GetMapping(value = "/books/searchByAuthor")
    public ResponseEntity<List<BookResponseDto>> getByAuthor(@RequestParam(name = "author") @NotBlank(message = "author name can't be blank/empty!!") String authorName){
        List<BookResponseDto> list = this.service.getBookByAuthor(authorName);
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping(value = "/books/searchByGenre")
    public ResponseEntity<List<BookResponseDto>> getByGenre(@RequestParam(name= "genre") @NotBlank(message = "Genre can't be empty / blank!!") String genreName){
        List<BookResponseDto> list = this.service.getBookByGenre(genreName);
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }
}
