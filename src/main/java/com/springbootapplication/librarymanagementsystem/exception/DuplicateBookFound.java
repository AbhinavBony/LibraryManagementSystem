package com.springbootapplication.librarymanagementsystem.exception;

public class DuplicateBookFound extends RuntimeException {
    public DuplicateBookFound(String message) {
        super(message);
    }
}
