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
            return repository.findByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                    userId, start, end);
        }
        return repository.findByUserIdOrderByTransactionDateDescIdDesc(userId);
    }

    public Transaction create(String userId, Transaction transaction) {
        transaction.setId(null);
        transaction.setUserId(userId);
        return repository.save(transaction);
    }

    public Transaction update(String userId, Long id, Transaction transaction) {
        Transaction existing = findOwnedTransaction(userId, id);
        existing.setTitle(transaction.getTitle());
        existing.setCategory(transaction.getCategory());
        existing.setAmount(transaction.getAmount());
        existing.setType(transaction.getType());
        existing.setTransactionDate(transaction.getTransactionDate());
        existing.setNotes(transaction.getNotes());
        return repository.save(existing);
    }

    public void delete(String userId, Long id) {
        repository.delete(findOwnedTransaction(userId, id));
    }

    public SummaryResponse summarize(String userId) {
        BigDecimal income = repository.sumAmountByUserIdAndType(userId, TransactionType.INCOME);
        BigDecimal expenses = repository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE);
        return new SummaryResponse(income, expenses, income.subtract(expenses));
    }

    private Transaction findOwnedTransaction(String userId, Long id) {
        return repository.findById(id)
                .filter(transaction -> userId.equals(transaction.getUserId()))
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public static class TransactionNotFoundException extends RuntimeException {
        public TransactionNotFoundException(Long id) {
            super("Transaction not found: " + id);
        }
    }
}
