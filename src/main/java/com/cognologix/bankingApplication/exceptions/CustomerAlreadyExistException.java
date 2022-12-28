package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class CustomerAlreadyExistException extends RuntimeException {
    private Integer code;
    private ErrorsForCustomer customerAlreadyExist;

    public CustomerAlreadyExistException() {
    }

    public CustomerAlreadyExistException(String message) {
        super(message);
    }

    public CustomerAlreadyExistException(ErrorsForCustomer customerAlreadyExist) {
        super(customerAlreadyExist.toString());
        this.code = customerAlreadyExist.getCode();
        this.customerAlreadyExist = customerAlreadyExist;
    }
}
