package com.financetracker.service;

import com.financetracker.dto.SummaryResponse;
import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> list(String userId, LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return repository.findByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(userId, start, end);
        }
        return repository.findByUserIdOrderByTransactionDateDescIdDesc(userId);
    }

    public Transaction create(String userId, Transaction transaction) {
        transaction.setId(null);
        transaction.setUserId(userId);
        return repository.save(transaction);
    }

    public Transaction update(String userId, Long id, Transaction transaction) {
        Transaction existing = repository.findById(id)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        existing.setTitle(transaction.getTitle());
        existing.setCategory(transaction.getCategory());
        existing.setAmount(transaction.getAmount());
        existing.setType(transaction.getType());
        existing.setTransactionDate(transaction.getTransactionDate());
        existing.setNotes(transaction.getNotes());
        return repository.save(existing);
    }

    public void delete(String userId, Long id) {
        Transaction existing = repository.findById(id)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        repository.delete(existing);
    }

    public SummaryResponse summarize(String userId) {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;
        for (Transaction transaction : repository.findByUserIdOrderByTransactionDateDescIdDesc(userId)) {
            if (transaction.getType() == TransactionType.INCOME) {
                income = income.add(transaction.getAmount());
            } else {
                expenses = expenses.add(transaction.getAmount());
            }
        }
        return new SummaryResponse(income, expenses, income.subtract(expenses));
    }
}
