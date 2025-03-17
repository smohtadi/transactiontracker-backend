package com.smohtadi.expenses.exceptions;

public class TransactionTypeNotFoundException extends RuntimeException {
    public TransactionTypeNotFoundException(Integer id) {
        super("Type with id " + id + " not found.");
    }
}
