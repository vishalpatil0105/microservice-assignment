package com.vishal.currencyexchangecommon;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "currency_exchange")
public class CurrencyExchange {
    @Id
    private Long id;
    private String currencyFrom; // Renamed from 'from'
    private String currencyTo; // Renamed from 'to'
    private BigDecimal conversionMultiple;
    private int environment;

    // Constructors, getters, setters...

    public CurrencyExchange() {
    }

    public CurrencyExchange(Long id, String currencyFrom, String currencyTo, BigDecimal conversionMultiple) {
        this.id = id;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.conversionMultiple = conversionMultiple;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyFrom() { // Updated getter
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) { // Updated setter
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() { // Updated getter
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) { // Updated setter
        this.currencyTo = currencyTo;
    }

    public BigDecimal getConversionMultiple() {
        return conversionMultiple;
    }

    public void setConversionMultiple(BigDecimal conversionMultiple) { // Missing setter added!
        this.conversionMultiple = conversionMultiple;
    }

    public int getEnvironment() {
        return environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }
}