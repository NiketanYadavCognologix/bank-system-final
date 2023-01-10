package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.BranchDto;
import com.cognologix.bankingApplication.dto.Responses.BankOperations.CreateBranchResponse;
import org.springframework.stereotype.Service;

@Service
public interface BankService {
    CreateBranchResponse createBranch(BranchDto branchDto);
}
