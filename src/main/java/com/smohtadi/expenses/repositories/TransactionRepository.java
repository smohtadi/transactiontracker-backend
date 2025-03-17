package com.smohtadi.expenses.repositories;

import com.smohtadi.expenses.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,
        Long> {
    public Transaction findByIdAndUserId(Long id, Long userid);
}
