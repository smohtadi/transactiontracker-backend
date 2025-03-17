package com.smohtadi.expenses.dtos;

public record TransactionDto(String id,
                             String description,
                             String amount,
                             String date,
                             CategoryDto category,
                             CurrencyDto currency,
                             TransactionTypeDto type,
                             UserDto user) {
}
