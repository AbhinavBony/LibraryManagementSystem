package com.springbootapplication.librarymanagementsystem.exception;

public class BookNotFound extends RuntimeException {
    public BookNotFound(String message) {
        super(message);
    }
}
