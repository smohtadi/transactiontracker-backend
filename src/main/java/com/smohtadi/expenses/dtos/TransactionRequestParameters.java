package com.smohtadi.expenses.dtos;

import java.time.LocalDateTime;

public record TransactionRequestParameters(String[] types,
                                           String startDate,
                                           String endDate,
                                           String term,
                                           String userid) {
}
