package com.smohtadi.expenses.exceptions;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(Long id) {
        super("Transaction with id " + id + " not found.");
    }
}
