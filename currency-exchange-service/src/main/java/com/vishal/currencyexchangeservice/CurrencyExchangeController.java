package com.vishal.currencyexchangeservice;

import com.vishal.currencyexchangecommon.CurrencyExchange;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/currency-exchange") // Base path for all mappings
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/from/{from}/to/{to}")
    public CurrencyExchange retrieveCurrencyExchange(@PathVariable String from, @PathVariable String to) {
        System.out.println("Fetching exchange rate for " + from + " to " + to);
        CurrencyExchange currencyExchange = repository.findByCurrencyFromAndCurrencyTo(from, to);

        if (currencyExchange == null) {
            System.out.println("No exchange rate found for " + from + " to " + to);
            throw new RuntimeException("Unable to find data for " + from + " to " + to);
        }

        int environmentPort = environment.getProperty("local.server.port") != null
                ? Integer.parseInt(environment.getProperty("local.server.port"))
                : 0;

        currencyExchange.setEnvironment(environmentPort);
        return currencyExchange;
    }

    // POST (Create) Mapping
    @PostMapping("/create")
    public ResponseEntity<CurrencyExchange> createCurrencyExchange(@RequestBody CurrencyExchange exchange) {
        System.out.println("Attempting to insert: " + exchange);
        if (exchange == null || exchange.getCurrencyFrom() == null || exchange.getCurrencyTo() == null
                || exchange.getConversionMultiple() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CurrencyExchange createdExchange = repository.save(exchange);
        return new ResponseEntity<>(createdExchange, HttpStatus.CREATED);
    }

    // PUT (Update) Mapping
    @PutMapping("/update")
    public ResponseEntity<CurrencyExchange> updateCurrencyExchange(@RequestBody CurrencyExchange exchange) {

        // Check if the exchange ID exists in the repository
        Optional<CurrencyExchange> existingExchange = repository.findById(exchange.getId());
        if (existingExchange.isPresent()) {
            CurrencyExchange updatedExchange = existingExchange.get();

            // Update fields with the values from the request body
            updatedExchange.setCurrencyFrom(exchange.getCurrencyFrom());
            updatedExchange.setCurrencyTo(exchange.getCurrencyTo());
            updatedExchange.setConversionMultiple(exchange.getConversionMultiple());
            updatedExchange.setEnvironment(exchange.getEnvironment());

            // Save the updated exchange object to the database
            CurrencyExchange savedExchange = repository.save(updatedExchange);

            return new ResponseEntity<>(savedExchange, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE Mapping
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurrencyExchange(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ResponseEntity<>("Currency exchange with id " + id + " has been deleted.",
                    HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Currency exchange with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    // GET by ID Mapping
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyExchange> getCurrencyExchange(@PathVariable Long id) {
        Optional<CurrencyExchange> optionalExchange = repository.findById(id);
        return optionalExchange.map(exchange -> new ResponseEntity<>(exchange, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
