package com.smohtadi.expenses.dtos;

public record TransactionUpdateDto(String description,
                                   String amount,
                                   String date,
                                   Long category,
                                   Integer currency,
                                   Integer type) {
}
