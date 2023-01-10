package com.cognologix.bankingApplication.dto.dtoToEntity;

import com.cognologix.bankingApplication.entities.banks.Bank;
import com.cognologix.bankingApplication.entities.banks.branch.Branch;

import java.util.List;

public class BankDtoToEntity {
    public Bank bankDtoBankEntity(Bank existingBank, List<Branch> foundBranches){
        Bank bank = new Bank();
        bank.setBankId(existingBank.getBankId());
        bank.setBankName(existingBank.getBankName());
        bank.setBranches(foundBranches);
        return bank;
    }
}
