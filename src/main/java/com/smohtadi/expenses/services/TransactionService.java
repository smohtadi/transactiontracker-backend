package com.smohtadi.expenses.services;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smohtadi.expenses.dtos.*;
import com.smohtadi.expenses.exceptions.*;
import com.smohtadi.expenses.models.*;
import com.smohtadi.expenses.repositories.*;
import com.smohtadi.expenses.utils.DateUtil;

@Service
public class TransactionService {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final CurrencyRepository currencyRepository;
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final TransactionTypeRepository transactionTypeRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EntityManager entityManager;

    public TransactionService(
            CategoryRepository categoryRepository,
            CurrencyRepository currencyRepository,
            EntityManager entityManager,
            TransactionRepository transactionRepository,
            TransactionTypeRepository transactionTypeRepository,
            UserRepository userRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
        this.entityManager = entityManager;
        this.transactionRepository = transactionRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.userRepository = userRepository;
    }

    public TransactionDto create(TransactionCreateDto payload, UserDto userDto) {
        Category category = categoryRepository.findById(payload.category()).orElseThrow(() -> new CategoryNotFoundException(payload.category()));
        Currency currency = currencyRepository.findById(payload.currency()).orElseThrow(() -> new CurrencyNotFoundException(payload.currency()));
        TransactionType transactionType = transactionTypeRepository.findById(payload.type()).orElseThrow(() -> new TransactionTypeNotFoundException(payload.type()));
        User user = userRepository.findById(Long.parseLong(userDto.id())).orElseThrow(() -> new UserNotFoundException(Long.parseLong(userDto.id())));
        Transaction transaction = new Transaction(
                payload.description(),
                new BigDecimal(payload.amount()),
                DateUtil.toUtc(payload.date() + " 00:00:00", DateUtil.VANCOUVER_TZ),
                category,
                currency,
                transactionType,
                user,
                DateUtil.utcNow(),
                DateUtil.utcNow()
        );
        transactionRepository.save(transaction);
        return toDto(transaction);
    }

    public void delete(Long id, UserDto userDto) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, Long.parseLong(userDto.id()));
        if (transaction == null) throw new TransactionNotFoundException(id);
        transactionRepository.delete(transaction);
    }

    public PagedResponse<TransactionDto> get(PagedRequest pagedRequest, TransactionRequestParameters params) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);
        cq.select(root);
        cq.orderBy(cb.asc(root.get("created")));
        if (params.term() != null && !params.term().isBlank()) {
            Predicate termPredicate = cb.like(root.get("description"), "%" + params.term() + "%");
            cq.where(termPredicate);
        }
        List<Predicate> predicates = new ArrayList<>();
        if (params.startDate() != null && !params.startDate().isBlank()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), DateUtil.toUtc(params.startDate(), DateUtil.VANCOUVER_TZ)));
        }
        if (params.endDate() != null && !params.endDate().isBlank()) {
            predicates.add(cb.lessThanOrEqualTo(root.get("date"), DateUtil.toUtc(params.endDate(), DateUtil.VANCOUVER_TZ)));
        }
        if (params.types() != null && params.types().length > 0) {
            Join<Transaction, TransactionType> join = root.join("transactionType");
            predicates.add(root.get("transactionType").get("slug").in(Arrays.asList(params.types())));
        }
        if (params.userid() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), Long.parseLong(params.userid())));
        }
        cq.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Transaction> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult(pagedRequest.getPage() - 1);
        typedQuery.setMaxResults(pagedRequest.getPageSize());
        List<Transaction> transactions = typedQuery.getResultList();
        List<TransactionDto> dtos = transactions.stream().map(this::toDto).toList();
        return new PagedResponse<TransactionDto>(dtos, 1, 20, dtos.size());
    }

    public TransactionDto getById(Long id, UserDto userDto) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, Long.parseLong(userDto.id()));
        return toDto(transaction);
    }

    public void update(TransactionUpdateDto payload, Long id, UserDto userDto) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, Long.parseLong(userDto.id()));
        if (transaction == null) throw new TransactionNotFoundException(id);
        Category category = categoryRepository.findById(payload.category()).orElseThrow(() -> new CategoryNotFoundException(payload.category()));
        Currency currency = currencyRepository.findById(payload.currency()).orElseThrow(() -> new CurrencyNotFoundException(payload.currency()));
        TransactionType type = transactionTypeRepository.findById(payload.type()).orElseThrow(() -> new TransactionTypeNotFoundException(payload.type()));
        transaction.setAmount(new BigDecimal(payload.amount()));
        transaction.setCategory(category);
        transaction.setCurrency(currency);
        transaction.setDate(DateUtil.toUtc(payload.date() + " 00:00:00", DateUtil.VANCOUVER_TZ
        ));
        transaction.setDescription(payload.description());
        transaction.setTransactionType(type);
        transactionRepository.save(transaction);
    }

    private TransactionDto toDto(Transaction transaction) {
        if (transaction == null) return null;
        CategoryDto categoryDto = transaction.getCategory() != null ?
                new CategoryDto(
                        transaction.getCategory().getId().toString(),
                        transaction.getCategory().getName()
                ) : null;
        CurrencyDto currencyDto = transaction.getCurrency() != null ?
                new CurrencyDto(
                        transaction.getCurrency().getId().toString(),
                        transaction.getCurrency().getName(),
                        transaction.getCurrency().getAbbr()
                ) : null;
        TransactionTypeDto transactionTypeDto =
                transaction.getTransactionType() != null ?
                        new TransactionTypeDto(
                                transaction.getTransactionType().getId().toString(),
                                transaction.getTransactionType().getName(),
                                transaction.getTransactionType().getSlug()
                        ) : null;
        UserDto userDto = transaction.getUser() != null ?
                new UserDto(
                        transaction.getUser().getId().toString(),
                        transaction.getUser().getUsername()
                ): null;
        return new TransactionDto(
                transaction.getId().toString(),
                transaction.getDescription(),
                transaction.getAmount().toString(),
                transaction.getDate().format(DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd")),
                categoryDto,
                currencyDto,
                transactionTypeDto,
                userDto
        );
    }
}
