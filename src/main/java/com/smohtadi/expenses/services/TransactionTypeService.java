package com.smohtadi.expenses.services;

import com.smohtadi.expenses.dtos.TransactionTypeDto;
import com.smohtadi.expenses.models.TransactionType;
import com.smohtadi.expenses.repositories.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionTypeService {
    @Autowired
    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    public List<TransactionTypeDto> get() {
        List<TransactionType> transactionTypes = transactionTypeRepository.findAll();
        return transactionTypes.stream().map(this::toDto).toList();
    }

    private TransactionTypeDto toDto(TransactionType transactionType) {
        return new TransactionTypeDto(
                transactionType.getId().toString(),
                transactionType.getName(),
                transactionType.getSlug()
        );
    }
}
