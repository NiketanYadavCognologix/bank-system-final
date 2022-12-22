package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class CustomerAlreadyExistException extends RuntimeException {
    private Integer code;
    private String Message;

    public CustomerAlreadyExistException() {
    }

    public CustomerAlreadyExistException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForCustomer.CUSTOMER_ALREADY_EXIST.getCode();
    }
}
