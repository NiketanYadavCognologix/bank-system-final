package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class DepositAmountResponse extends BaseResponse {
    public DepositAmountResponse(Boolean success, String message) {
        super(success);
        this.setMessage(message);
    }
}
