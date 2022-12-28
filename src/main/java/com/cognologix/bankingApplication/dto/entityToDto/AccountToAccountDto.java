package com.cognologix.bankingApplication.dto.entityToDto;


import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.entities.Account;

public class AccountToAccountDto {
    private Long accountNumber;
    private String accountType;
    private Double balance;
    private Integer customerId;

    public AccountDto entityToDto(Account account){
        AccountDto accountDto=new AccountDto();
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBalance(account.getBalance());
        accountDto.setCustomerId(account.getCustomer().getCustomerId());
        return accountDto;
    }
}
