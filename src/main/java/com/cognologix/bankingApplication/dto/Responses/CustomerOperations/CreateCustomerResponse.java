package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Customer;

public class CreateCustomerResponse extends BaseResponse {
    private Customer customer;
    public CreateCustomerResponse(Boolean success, String message, Customer customer) {
        super(success);
        this.setMessage(message);
        this.customer=customer;
    }
}
