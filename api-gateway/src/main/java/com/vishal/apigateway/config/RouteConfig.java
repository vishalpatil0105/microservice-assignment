package com.vishal.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(RouteConfig.class);
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        logger.info("Configuring API Gateway routes");
        
        return builder.routes()
            // Direct route to currency exchange service without authentication (for testing)
            .route("direct_currency_exchange", r -> r.path("/direct/**")
                .filters(f -> f.stripPrefix(1))
                .uri("http://localhost:8000"))
            
            // External test route to httpbin
            .route("httpbin_route", r -> r.path("/api/**")
                .filters(f -> f.stripPrefix(1))
                .uri("https://httpbin.org"))
            
            // Authenticated route to currency exchange service
            .route("currency_exchange_route", r -> r.path("/currency-exchange/**")
                .uri("http://localhost:8000"))
                
            // Route to currency conversion service
            .route("currency_conversion_route", r -> r.path("/currency-conversion/**")
                .uri("http://localhost:8100"))
                
            .build();
    }
} 