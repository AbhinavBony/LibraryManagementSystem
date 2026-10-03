package com.springbootapplication.librarymanagementsystem.service;

import com.springbootapplication.librarymanagementsystem.dto.RegisterRequestDTO;
import com.springbootapplication.librarymanagementsystem.dto.UserResponseDTO;
import com.springbootapplication.librarymanagementsystem.entity.user.User;
import com.springbootapplication.librarymanagementsystem.exception.UserAlreadyExistsException;
import com.springbootapplication.librarymanagementsystem.interfaces.AuthService;
import com.springbootapplication.librarymanagementsystem.models.JwtRequest;
import com.springbootapplication.librarymanagementsystem.models.JwtResponse;
import com.springbootapplication.librarymanagementsystem.repository.UserRepository;
import com.springbootapplication.librarymanagementsystem.security.JWTHelper;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;
    private final JWTHelper helper;
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    public AuthServiceImpl(UserDetailsService userDetailsService, AuthenticationManager manager, JWTHelper helper, UserRepository repo, PasswordEncoder encoder, ModelMapper mapper) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.helper = helper;
        this.repo = repo;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public JwtResponse login(JwtRequest request) {
        this.doAuthenticate(request.getUsername(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth-> auth.replace("ROLE_", ""))
                .orElse(null);
        return JwtResponse.builder().jwtToken(token).username(userDetails.getUsername()).role(role).build();
    }

    @Override
    public String register(RegisterRequestDTO dto) {
        repo.findByUsername(dto.getUsername()).ifPresent(existing -> {
            throw new UserAlreadyExistsException("Username already exists: " + dto.getUsername());
        });

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        repo.save(user);
        return "User registered successfully!";
    }

    @Override
    public List<UserResponseDTO> getAllUserDetails() {
        return repo.findAll().stream().map(user -> this.mapper.map(user, UserResponseDTO.class)).toList();
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        manager.authenticate(authentication);
    }
}
