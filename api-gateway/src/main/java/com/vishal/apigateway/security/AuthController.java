package com.vishal.apigateway.security;

import com.vishal.currencyexchangecommon.security.AuthRequest;
import com.vishal.currencyexchangecommon.security.AuthResponse;
import com.vishal.currencyexchangecommon.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/authenticate/test")
    public Mono<String> testAuthentication() {
        return Mono.just("Authentication endpoint is accessible");
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<?>> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            boolean isAuthenticated = userService.authenticate(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );

            if (!isAuthenticated) {
                return Mono.just(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Invalid credentials")
                );
            }

            final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            final String token = jwtUtil.generateToken(userDetails);

            return Mono.just(ResponseEntity.ok(new AuthResponse(token)));
        } catch (BadCredentialsException e) {
            return Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid credentials")
            );
        }
    }
} 