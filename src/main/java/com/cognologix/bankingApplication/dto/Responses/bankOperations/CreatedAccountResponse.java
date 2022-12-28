package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedAccountResponse extends BaseResponse {

    private String customerName;
    private Long accountNumber;
    private String accountType;
    private String status;
    private Double balance;

    public CreatedAccountResponse(Boolean success, String message, Account account) {
        super(success);
        this.setMessage(message);
        customerName=account.getCustomer().getCustomerName();
        accountNumber=account.getAccountNumber();
        accountType=account.getAccountType();
        status=account.getStatus();
        balance=account.getBalance();
    }

    public CreatedAccountResponse() {
        super(true);
    }
}
