package com.financetracker.controller;

import com.financetracker.dto.SummaryResponse;
import com.financetracker.model.Transaction;
import com.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> list(
            Principal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return service.list(principal.getName(), start, end);
    }

    @GetMapping("/summary")
    public SummaryResponse summary(Principal principal) {
        return service.summarize(principal.getName());
    }

    @PostMapping
    public Transaction create(Principal principal, @Valid @RequestBody Transaction transaction) {
        return service.create(principal.getName(), transaction);
    }

    @PutMapping("/{id}")
    public Transaction update(Principal principal, @PathVariable Long id, @Valid @RequestBody Transaction transaction) {
        return service.update(principal.getName(), id, transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable Long id) {
        service.delete(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
