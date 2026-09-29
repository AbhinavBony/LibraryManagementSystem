package com.springbootapplication.librarymanagementsystem.config;

import com.springbootapplication.librarymanagementsystem.dto.BookRequestDto;
import com.springbootapplication.librarymanagementsystem.entity.book.Book;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookMapper {
    @Bean
    public ModelMapper mapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(BookRequestDto.class, Book.class).addMappings(m-> m.skip(Book::setId));
        return mapper;
    }
}
