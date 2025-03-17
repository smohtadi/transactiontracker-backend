package com.smohtadi.expenses.dtos;

public record TransactionCreateDto(String description,
                                   String amount,
                                   String date,
                                   Long category,
                                   Integer currency,
                                   Integer type) {
}
