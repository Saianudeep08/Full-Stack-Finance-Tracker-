package com.financetracker.repository;

import com.financetracker.model.Transaction;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByTransactionDateDescIdDesc(String userId);
    List<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(String userId, LocalDate start, LocalDate end);
}
