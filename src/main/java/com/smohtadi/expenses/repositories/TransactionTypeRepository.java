package com.smohtadi.expenses.repositories;

import com.smohtadi.expenses.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> { }
