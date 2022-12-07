package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class DeactivateAccountResponse extends BaseResponse {
    public DeactivateAccountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
