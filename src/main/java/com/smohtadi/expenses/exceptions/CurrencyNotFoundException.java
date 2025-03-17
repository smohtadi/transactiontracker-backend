package com.smohtadi.expenses.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(Integer id) {
        super("Currency with id " + id + " not found.");
    }
}
