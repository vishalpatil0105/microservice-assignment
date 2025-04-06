package com.vishal.apigateway.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private final Map<String, String> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        // Add a default user for testing purposes
        String encodedPassword = passwordEncoder.encode("password");
        users.put("user", encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String encodedPassword = users.get(username);
        if (encodedPassword == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new User(username, encodedPassword, new ArrayList<>());
    }

    public boolean authenticate(String username, String rawPassword) {
        String encodedPassword = users.get(username);
        if (encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 