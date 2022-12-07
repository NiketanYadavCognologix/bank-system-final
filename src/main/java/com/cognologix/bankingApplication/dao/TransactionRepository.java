package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<BankTransaction, Integer> {
    @Query(value = "SELECT * FROM bank_transaction" +
            " WHERE from_account_number=?1 OR to_account_number=?1", nativeQuery = true)
    List<BankTransaction> findByToAccountNumberEquals(Long toAccountNumber);

}
