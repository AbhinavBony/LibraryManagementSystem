package com.springbootapplication.librarymanagementsystem.interfaces;

import com.springbootapplication.librarymanagementsystem.dto.RegisterRequestDTO;
import com.springbootapplication.librarymanagementsystem.dto.UserResponseDTO;

import com.springbootapplication.librarymanagementsystem.models.JwtRequest;
import com.springbootapplication.librarymanagementsystem.models.JwtResponse;

import java.util.List;

public interface AuthService {
    JwtResponse login(JwtRequest request);
    String register(RegisterRequestDTO dto);
    List<UserResponseDTO> getAllUserDetails();
}
