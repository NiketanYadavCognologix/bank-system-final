package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.banks.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank,Integer> {
    Bank findByBankNameEquals(String bankName);
}
