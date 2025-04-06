package com.vishal.currencyconversionservice.config;

import com.vishal.currencyconversionservice.security.JwtRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public JwtRequestInterceptor jwtRequestInterceptor() {
        return new JwtRequestInterceptor();
    }
} 