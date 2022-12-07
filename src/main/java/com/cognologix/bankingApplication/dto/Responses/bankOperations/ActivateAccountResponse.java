package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class ActivateAccountResponse extends BaseResponse {
    public ActivateAccountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
