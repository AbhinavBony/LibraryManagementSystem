package com.springbootapplication.librarymanagementsystem.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {
    @NotBlank(message = "username can't be empty / blank!!")
    private String username;
    @NotBlank(message = "password can't be empty / blank!!")
    private String password;
}
