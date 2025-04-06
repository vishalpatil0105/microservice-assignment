package com.vishal.currencyexchangecommon.security;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
} 