package com.cognologix.bankingApplication.dto.dtoToEntity;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.enums.AccountStatus;

public class AccountDtoToEntity {

    public Account accountDtoToAccountEntity(AccountDto accountDto, Customer customer, String ifscCode) {
        String accountType = accountDto.getAccountType();
        String status = AccountStatus.ACTIVATED.toString();
        Double balance = accountDto.getBalance();
        String bankName = accountDto.getBankName();
        String branch = accountDto.getBranch();

        Account newAccount = new Account(null, bankName, branch, ifscCode, status, accountType, balance, customer);
        return newAccount;
    }
}
