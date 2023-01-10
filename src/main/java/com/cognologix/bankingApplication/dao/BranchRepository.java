package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.banks.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch,Integer> {
    Branch findByBranchEquals(String branch);

    List<Branch> findByBankNameEquals(String bankName);
}
