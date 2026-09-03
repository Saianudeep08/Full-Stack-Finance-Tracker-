package com.financetracker.repository;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByTransactionDateDescIdDesc(String userId);

    List<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
            String userId, LocalDate start, LocalDate end);

    @Query("select coalesce(sum(t.amount), 0) from Transaction t where t.userId = :userId and t.type = :type")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") String userId, @Param("type") TransactionType type);
}
