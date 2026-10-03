package com.springbootapplication.librarymanagementsystem.utils;

import com.springbootapplication.librarymanagementsystem.exception.BookNotFound;
import com.springbootapplication.librarymanagementsystem.exception.DuplicateBookFound;
import com.springbootapplication.librarymanagementsystem.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(DuplicateBookFound.class)
    public ResponseEntity<ErrorResponse> handleDuplicateBookException(DuplicateBookFound e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(BookNotFound.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgsException(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        response.put("errorCode", HttpStatus.BAD_REQUEST.value());
        response.put("message", error);
        response.put("timeStamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password", LocalDateTime.now()));
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), LocalDateTime.now()));
    }
}
