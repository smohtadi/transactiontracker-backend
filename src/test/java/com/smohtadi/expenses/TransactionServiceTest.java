package com.smohtadi.expenses;

import com.smohtadi.expenses.dtos.*;
import com.smohtadi.expenses.models.*;
import com.smohtadi.expenses.repositories.CategoryRepository;
import com.smohtadi.expenses.repositories.CurrencyRepository;
import com.smohtadi.expenses.repositories.TransactionRepository;
import com.smohtadi.expenses.repositories.TransactionTypeRepository;
import com.smohtadi.expenses.services.TransactionService;
import com.smohtadi.expenses.utils.DateUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionTypeRepository transactionTypeRepository;
    @Mock
    EntityManager entityManager;
    private TransactionService transactionService;
    @BeforeEach
    public void setUp() throws Exception{
        transactionService = new TransactionService(categoryRepository, currencyRepository, entityManager, transactionRepository, transactionTypeRepository);
    }
    @Test
    public void getTransaction_returnsTransaction() {
        Category category = new Category("Food", "food", DateUtil.utcNow(), DateUtil.utcNow());
        category.setId(1L);
        CategoryDto categoryDto = new CategoryDto("1", "Food");

        Currency currency = new Currency("Canadian Dollar", "canadian-dollar", "CAD", DateUtil.utcNow(), DateUtil.utcNow());
        currency.setId(2);
        CurrencyDto currencyDto = new CurrencyDto("2", "Canadian Dollar", "CAD");

        TransactionType type = new TransactionType("Expense", "expense", DateUtil.utcNow(), DateUtil.utcNow());
        type.setId(3);
        TransactionTypeDto typeDto = new TransactionTypeDto("3", "Expense", "expense");

        User user = new User("john@me.ca", "xyz", DateUtil.utcNow(), DateUtil.utcNow());
        user.setId(4L);
        UserDto userDto = new UserDto("4", "john@me.ca");

        LocalDateTime transactionDate = DateUtil.utcNow();
        Transaction transaction = new Transaction("AAA", new BigDecimal("23.04"), transactionDate, category, currency, type, user, DateUtil.utcNow(), DateUtil.utcNow());
        transaction.setId(1L);
        TransactionDto expected = new TransactionDto("1", "AAA", "23.04", DateUtil.format(transactionDate), categoryDto, currencyDto, typeDto, userDto);

        given(transactionRepository.findById(1L)).willReturn(Optional.of(transaction));
        TransactionDto actual = transactionService.getById(1L);
        assertThat(actual).isEqualTo(expected);
    }
}
