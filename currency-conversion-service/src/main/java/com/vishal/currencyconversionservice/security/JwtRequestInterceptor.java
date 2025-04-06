package com.vishal.currencyconversionservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class JwtRequestInterceptor implements RequestInterceptor {

    @Value("${jwt.token:#{null}}")
    private String token;

    @Override
    public void apply(RequestTemplate template) {
        if (token != null && !token.isEmpty()) {
            template.header("Authorization", "Bearer " + token);
        }
    }

    public void setToken(String token) {
        this.token = token;
    }
} 