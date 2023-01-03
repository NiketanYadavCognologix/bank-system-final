package com.cognologix.bankingApplication.dto.Responses.accountOperations;

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
public class DeactivatedAccountsResponse extends BaseResponse {

    private List<AccountDto> deactivatedAccountsResponse=new ArrayList<>();

    public DeactivatedAccountsResponse(Boolean success, String message, List<Account> deactivatedAccounts) {
        super(success);
        this.setMessage(message);
        for (Account account : deactivatedAccounts){
            AccountDto accountDto=new AccountToAccountDto().entityToDto(account);
            deactivatedAccountsResponse.add(accountDto);
        }
    }

    public DeactivatedAccountsResponse() {
        super(true);
    }
}
