package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.dto.entityToDto.AccountToAccountDto;
import com.cognologix.bankingApplication.entities.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetAllAccountsForCustomerResponse extends BaseResponse {
    List<AccountDto> accountsInResponse=new ArrayList<>();

    public GetAllAccountsForCustomerResponse(Boolean success, String message, List<Account> accounts) {
        super(success);
        this.setMessage(message);
        for (Account account : accounts){
            AccountDto accountDto=new AccountToAccountDto().entityToDto(account);
            accountsInResponse.add(accountDto);
        }
    }

    public GetAllAccountsForCustomerResponse(){
        super(true);
    }
}
