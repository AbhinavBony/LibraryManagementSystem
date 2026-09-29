package com.springbootapplication.librarymanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {
    @NotBlank(message = "book title can't be blank!!")
    private String title;
    @NotBlank(message = "book author can't be blank!!")
    private String author;
    @NotBlank(message = "book genre can't be blank!!")
    private String genre;
    @Positive(message = "book price can't be negative or zero!!")
    @NotNull(message = "book price can't be null!!")
    private Double price;
}
