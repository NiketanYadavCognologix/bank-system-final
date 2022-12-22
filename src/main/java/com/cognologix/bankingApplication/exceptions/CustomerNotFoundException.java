package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException {
    private Integer code;
    private String Message;

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForCustomer.CUSTOMER_NOT_FOUND.getCode();
    }
}
