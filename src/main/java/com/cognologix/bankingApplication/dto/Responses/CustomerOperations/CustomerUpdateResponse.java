package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;

public class CustomerUpdateResponse extends BaseResponse{

    public CustomerUpdateResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}