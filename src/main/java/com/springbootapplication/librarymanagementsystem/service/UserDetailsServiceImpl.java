package com.springbootapplication.librarymanagementsystem.service;

import com.springbootapplication.librarymanagementsystem.entity.user.User;
import com.springbootapplication.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: "+ username));
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername()).password(user.getPassword()).authorities((new SimpleGrantedAuthority("ROLE_"+ user.getRole().name()))).build();
    }
}
