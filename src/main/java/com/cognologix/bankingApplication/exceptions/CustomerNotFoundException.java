package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException {
    private Integer code;
//    private String message;

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
//        this.message = ErrorsForCustomer.valueOf(message).getMessage();
        this.code = ErrorsForCustomer.valueOf(message).getCode();
    }
}
