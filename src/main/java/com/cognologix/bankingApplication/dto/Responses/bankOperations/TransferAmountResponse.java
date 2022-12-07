package com.cognologix.bankingApplication.dto.Responses.bankOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class TransferAmountResponse extends BaseResponse {
    public TransferAmountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
