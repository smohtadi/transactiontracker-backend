package com.smohtadi.expenses.controllers;

import com.smohtadi.expenses.dtos.CurrencyDto;
import com.smohtadi.expenses.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencies/")
public class CurrencyController {
    @Autowired
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public List<CurrencyDto> get() {
        return currencyService.get();
    }
}
