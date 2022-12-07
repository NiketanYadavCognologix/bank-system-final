package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Account;

import java.util.List;

public class DeactivatedAccountsResponse extends BaseResponse {

    private List<Account> deactivatedAccounts;

    public DeactivatedAccountsResponse(Boolean success, String message, List<Account> deactivatedAccounts) {
        super(success);
        this.setMessage(message);
        this.deactivatedAccounts = deactivatedAccounts;
    }
}
