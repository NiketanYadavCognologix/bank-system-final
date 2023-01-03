package com.cognologix.bankingApplication.dto.Responses.BankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class CreateBranchResponse extends BaseResponse {
    public CreateBranchResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
