package com.vishal.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @GetMapping("/gateway-test")
    public Mono<String> testGateway() {
        logger.info("Gateway test endpoint called");
        return Mono.just("API Gateway is working correctly");
    }
} 