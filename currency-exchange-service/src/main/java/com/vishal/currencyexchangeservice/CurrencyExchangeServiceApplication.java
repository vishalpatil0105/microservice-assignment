package com.vishal.currencyexchangeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.domain.EntityScan; // Import this

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.vishal.currencyexchangecommon") // Add this annotation
public class CurrencyExchangeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeServiceApplication.class, args);
    }
}