package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Customer;

import java.util.List;

public class GetAllCustomerResponse extends BaseResponse {
    private List<Customer> customers;
    public GetAllCustomerResponse(Boolean success, List<Customer> customers) {
        super(success);
        this.customers=customers;
    }
}
