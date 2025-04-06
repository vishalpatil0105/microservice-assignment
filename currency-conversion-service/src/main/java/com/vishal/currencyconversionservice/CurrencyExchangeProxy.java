package com.vishal.currencyconversionservice;

import com.vishal.currencyexchangecommon.CurrencyExchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange-service")
public interface CurrencyExchangeProxy {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyExchange retrieveCurrencyExchange(
            @PathVariable("from") String from, // Added "from"
            @PathVariable("to") String to); // Added "to"
}