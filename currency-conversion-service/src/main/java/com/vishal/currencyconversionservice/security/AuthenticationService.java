package com.vishal.currencyconversionservice.security;

import com.vishal.currencyexchangecommon.security.AuthRequest;
import com.vishal.currencyexchangecommon.security.AuthResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {

    @Value("${jwt.username:user}")
    private String username;

    @Value("${jwt.password:password}")
    private String password;

    @Autowired
    private JwtRequestInterceptor jwtRequestInterceptor;

    @Autowired
    private Environment environment;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        refreshToken();
    }

    public void refreshToken() {
        try {
            String gatewayUrl = "http://localhost:8765"; // API Gateway URL
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            AuthRequest request = new AuthRequest(username, password);
            
            HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    gatewayUrl + "/authenticate", 
                    entity, 
                    AuthResponse.class
            );
            
            if (response.getBody() != null) {
                String token = response.getBody().getToken();
                jwtRequestInterceptor.setToken(token);
                
                // Also set it in System property for other beans that might need it
                System.setProperty("jwt.token", token);
            }
        } catch (Exception e) {
            System.err.println("Failed to obtain JWT token: " + e.getMessage());
        }
    }
} 