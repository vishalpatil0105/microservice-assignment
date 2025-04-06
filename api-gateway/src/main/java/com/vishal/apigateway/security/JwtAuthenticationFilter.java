package com.vishal.apigateway.security;

import com.vishal.currencyexchangecommon.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        
        logger.debug("Processing request: {} {}", request.getMethod(), path);
        
        // Skip filter for permitted paths
        if (path.startsWith("/authenticate") || 
            path.startsWith("/gateway-test") || 
            path.startsWith("/direct/") || 
            path.startsWith("/api/") || 
            path.startsWith("/test/")) {
            logger.debug("Skipping JWT authentication for permitted path: {}", path);
            return chain.filter(exchange);
        }
        
        // Extract the token from the Authorization header
        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            logger.debug("Found Authorization header for path: {}", path);
            
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                try {
                    // Extract username from token first
                    String username = jwtUtil.getUsernameFromToken(token);
                    logger.debug("Extracted username from token: {}", username);
                    
                    // Load user details
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    
                    // Validate token with user details
                    if (jwtUtil.validateToken(token, userDetails)) {
                        logger.debug("JWT token validated successfully for user: {}", username);
                        
                        // Create authentication token
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder
                                        .withAuthentication(authentication));
                    } else {
                        logger.error("Token validation failed for user: {}", username);
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                } catch (Exception e) {
                    logger.error("JWT token validation failed: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
        }
        
        logger.warn("No valid Authorization header found for protected path: {}", path);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
} 