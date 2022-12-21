package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateResponse extends BaseResponse{

    Customer updatedCustomer;
    public CustomerUpdateResponse(Boolean success, String message, Customer customer) {
        super(success);
        this.setMessage(message);
        updatedCustomer=customer;
    }

    public CustomerUpdateResponse() {
        super(true);
    }
}