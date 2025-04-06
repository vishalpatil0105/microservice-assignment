package com.vishal.currencyconversionservice;

import com.vishal.currencyexchangecommon.CurrencyExchange;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {
    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name = "currency-conversion", fallbackMethod = "fallbackCalculateCurrencyConversion")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {
        CurrencyExchange currencyExchange = proxy.retrieveCurrencyExchange(from, to);
        BigDecimal conversionMultiple = currencyExchange.getConversionMultiple();
        BigDecimal totalCalculatedAmount = quantity.multiply(conversionMultiple);

        return new CurrencyConversion(
                currencyExchange.getId(),
                from,
                to,
                conversionMultiple,
                quantity,
                totalCalculatedAmount,
                currencyExchange.getEnvironment() // This should be an int
        );
    }

    public CurrencyConversion fallbackCalculateCurrencyConversion(
            String from, 
            String to, 
            BigDecimal quantity,
            Throwable t) {
        return new CurrencyConversion(0L, from, to, BigDecimal.ZERO, quantity, BigDecimal.ZERO, 0);
    }
}