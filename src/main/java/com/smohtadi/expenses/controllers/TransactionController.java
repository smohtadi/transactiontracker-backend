package com.smohtadi.expenses.controllers;

import com.smohtadi.expenses.dtos.*;
import com.smohtadi.expenses.exceptions.TransactionNotFoundException;
import com.smohtadi.expenses.models.User;
import com.smohtadi.expenses.services.TransactionService;
import com.smohtadi.expenses.services.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    public TransactionService transactionService;
    @Autowired
    public UserService userService;

    @PostMapping("/")
    public TransactionDto create(@RequestBody TransactionCreateDto payload, @NotNull Authentication authentication) {
        return transactionService.create(payload, getAuthUser(authentication));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @NotNull Authentication authentication) {
        transactionService.delete(id, getAuthUser(authentication));
    }

    @GetMapping("/")
    public ResponseEntity<List<TransactionDto>> getAll(
            @RequestParam(name = "page") @Min(1) int page,
            @RequestParam(name = "size") @Min(1) @Max(20) int size,
            @RequestParam(name = "sort", defaultValue = "date-asc") String sort,
            @RequestParam(name = "term", required = false) String qTerm,
            @RequestParam(name = "types", required = false) String qTypes,
            @RequestParam(name = "to") String qEndDate,
            @RequestParam(name = "from") String qStartDate,
            @NotNull Authentication authentication
    ) {
        UserDto user = userService.getByUid(authentication.getPrincipal().toString());
        PagedRequest pagedRequest = PagedRequest.of(page, size, sort);
        TransactionRequestParameters reqParams = new TransactionRequestParameters(
                !qTypes.isBlank() ? qTypes.split(",") : new String[0],
                qStartDate,
                qEndDate,
                qTerm,
                user.id()
        );
        PagedResponse<TransactionDto> pagedResponse = transactionService.get(pagedRequest, reqParams);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Pagination", "count=" + pagedResponse.totalElements() + ";" +
                        "size=" + pagedResponse.pageSize() + ";" +
                        "page=" + pagedResponse.pageNumber() + ";");
        return ResponseEntity.ok().headers(headers).body(pagedResponse.content());
    }

    @GetMapping("/{id}")
    public TransactionDto get(@PathVariable Long id, @NotNull Authentication authentication) {
        return transactionService.getById(id, getAuthUser(authentication));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody TransactionUpdateDto payload, @PathVariable Long id, @NotNull Authentication authentication) {
        transactionService.update(payload, id, getAuthUser(authentication));
    }

    public UserDto getAuthUser(Authentication authentication) {
        return userService.getByUid(authentication.getPrincipal().toString());
    }
}
