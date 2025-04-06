package com.vishal.currencyexchangeservice;

import com.vishal.currencyexchangecommon.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
    CurrencyExchange findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo); // Corrected method name
}