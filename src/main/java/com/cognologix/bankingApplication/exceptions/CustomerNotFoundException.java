package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException {
    private Integer code;
    private ErrorsForCustomer customerNotFound;

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(ErrorsForCustomer customerNotFound) {
        super(customerNotFound.toString());
        this.code = customerNotFound.getCode();
        this.customerNotFound = customerNotFound;
    }
}
