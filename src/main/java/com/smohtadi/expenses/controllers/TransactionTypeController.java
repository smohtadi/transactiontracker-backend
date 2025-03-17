package com.smohtadi.expenses.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.smohtadi.expenses.services.TransactionTypeService;
import com.smohtadi.expenses.dtos.TransactionTypeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction-types/")
public class TransactionTypeController {
    @Autowired
    private final TransactionTypeService transactionTypeService;

    public TransactionTypeController(TransactionTypeService transactionTypeService) {
        this.transactionTypeService = transactionTypeService;
    }

    @GetMapping("/")
    public List<TransactionTypeDto> get() {
        return transactionTypeService.get();
    }
}
