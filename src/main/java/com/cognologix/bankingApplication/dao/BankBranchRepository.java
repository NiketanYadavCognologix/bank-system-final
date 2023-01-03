package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.banks.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankBranchRepository extends JpaRepository<Branch,Integer> {
    Branch findByBranchEquals(String branch);
}
