package com.springbootapplication.librarymanagementsystem.service;

import com.springbootapplication.librarymanagementsystem.dto.RegisterRequestDTO;
import com.springbootapplication.librarymanagementsystem.dto.UserResponseDTO;
import com.springbootapplication.librarymanagementsystem.entity.user.Role;
import com.springbootapplication.librarymanagementsystem.entity.user.User;
import com.springbootapplication.librarymanagementsystem.exception.UserAlreadyExistsException;
import com.springbootapplication.librarymanagementsystem.models.JwtRequest;
import com.springbootapplication.librarymanagementsystem.models.JwtResponse;
import com.springbootapplication.librarymanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceImplTest {
    @Autowired
    private AuthServiceImpl service;
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;

    private RegisterRequestDTO registerDTO;
    @BeforeEach
    void setUp() {
        registerDTO = new RegisterRequestDTO();
        registerDTO.setUsername("AbhinavAdmin");
        registerDTO.setPassword("Abhinav@123");
        registerDTO.setRole(Role.LIBRARIAN);
    }
    @Test
    void register_shouldSaveUser_withEncodedPassword(){
        service.register(registerDTO);
        User saved = repo.findByUsername(registerDTO.getUsername()).orElseThrow();
        assertEquals(registerDTO.getUsername(), saved.getUsername());
        assertNotEquals(registerDTO.getPassword(), saved.getPassword());
        assertTrue(encoder.matches(registerDTO.getPassword(), saved.getPassword()));
        assertEquals(registerDTO.getRole(), saved.getRole());
    }
    @Test
    void register_shouldThrowException_whenUserExists(){
        service.register(registerDTO);

        assertThrows(UserAlreadyExistsException.class, ()-> service.register(registerDTO));
    }
    @Test
    void login_shouldReturnTokenUserNameAndRole_whenCredentialsValid(){
        service.register(registerDTO);
        JwtRequest request = new JwtRequest(registerDTO.getUsername(), registerDTO.getPassword());
        JwtResponse response = service.login(request);

        assertNotNull(response.getJwtToken());
        assertEquals(registerDTO.getUsername(), response.getUsername());
        assertEquals(registerDTO.getRole().name(), response.getRole());
    }
    @Test
    void login_shouldThrowException_whenPasswordIncorrect(){
        service.register(registerDTO);
        JwtRequest request = new JwtRequest(registerDTO.getUsername(), "Abhinav@231218");

        assertThrows(BadCredentialsException.class, ()-> service.login(request));
    }
    @Test
    void login_shouldThrowException_whenUsernameIncorrect(){
        service.register(registerDTO);
        JwtRequest request = new JwtRequest("Abhinav12Admin",  registerDTO.getPassword());

        assertThrows(BadCredentialsException.class, ()-> service.login(request));
    }
    @Test
    void getAllUsers_shouldReturnAllUsers_withoutPasswordField(){
        service.register(registerDTO);
        RegisterRequestDTO secondUser = new RegisterRequestDTO();
        secondUser.setUsername("Bony");
        secondUser.setPassword("Bony@123");
        secondUser.setRole(Role.USER);
        service.register(secondUser);

        List<UserResponseDTO> list = service.getAllUserDetails();
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(name-> name.getUsername().equals(registerDTO.getUsername())));
        assertTrue(list.stream().anyMatch(name-> name.getUsername().equals(secondUser.getUsername())));
    }
    @Test
    void getAllUsers_shouldThrowException_whenListIsEmpty(){
        List<UserResponseDTO> list = service.getAllUserDetails();

        assertTrue(list.isEmpty());
    }
}