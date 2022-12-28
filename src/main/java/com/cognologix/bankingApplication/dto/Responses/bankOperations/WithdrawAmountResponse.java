package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class WithdrawAmountResponse extends BaseResponse {
    public WithdrawAmountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
