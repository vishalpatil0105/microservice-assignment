package com.vishal.currencyexchangecommon.security;

import java.io.Serializable;

public class AuthRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    
    private String username;
    private String password;
    
    // Default constructor for JSON Parsing
    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 