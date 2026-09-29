package com.springbootapplication.librarymanagementsystem.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {
    private String username;
    private String password;
}
