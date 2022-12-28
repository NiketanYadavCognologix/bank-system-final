package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class BalanceInquiryResponse extends BaseResponse{
    public BalanceInquiryResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
