package com.smohtadi.expenses.repositories;

import com.smohtadi.expenses.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
