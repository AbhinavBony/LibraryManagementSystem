package com.springbootapplication.librarymanagementsystem.controller;

import com.springbootapplication.librarymanagementsystem.dto.RegisterRequestDTO;
import com.springbootapplication.librarymanagementsystem.dto.UserResponseDTO;
import com.springbootapplication.librarymanagementsystem.entity.user.User;
import com.springbootapplication.librarymanagementsystem.interfaces.AuthService;
import com.springbootapplication.librarymanagementsystem.models.JwtRequest;
import com.springbootapplication.librarymanagementsystem.models.JwtResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/auth")
@Validated
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid JwtRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO dto){
        return new ResponseEntity<>(this.service.register(dto), HttpStatus.CREATED);
    }
    @GetMapping(value = "/getUsers")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> userList = this.service.getAllUserDetails();
        if (userList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(userList);
    }
}
