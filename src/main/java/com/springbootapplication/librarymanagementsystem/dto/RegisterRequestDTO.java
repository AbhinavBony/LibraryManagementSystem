package com.springbootapplication.librarymanagementsystem.dto;

import com.springbootapplication.librarymanagementsystem.entity.user.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "username can't be blank/empty!!")
    private String username;
    @NotBlank(message = "password can't be blank/empty!!")
    private String password;
    @NotNull(message = "role can't be blank/empty!!")
    private Role role;
}
