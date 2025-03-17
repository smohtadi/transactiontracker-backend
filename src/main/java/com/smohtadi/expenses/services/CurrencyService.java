package com.smohtadi.expenses.services;

import com.smohtadi.expenses.dtos.CurrencyDto;
import com.smohtadi.expenses.models.Currency;
import com.smohtadi.expenses.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyDto> get() {
        return currencyRepository.findAll().stream().map(this::toDto).toList();
    }

    private CurrencyDto toDto(Currency currency) {
        return new CurrencyDto(currency.getId().toString(), currency.getName(), currency.getAbbr());
    }
}
