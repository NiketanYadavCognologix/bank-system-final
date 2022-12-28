package com.cognologix.bankingApplication.dto.Responses.CustomerOperations;

import com.cognologix.bankingApplication.dto.Responses.BaseResponse;
import com.cognologix.bankingApplication.entities.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerResponse extends BaseResponse {
    private Customer customer;
    public CreateCustomerResponse(Boolean success, String message, Customer customer) {
        super(success);
        this.setMessage(message);
        this.customer=customer;
    }

    public CreateCustomerResponse() {
        super(true);
    }
}
