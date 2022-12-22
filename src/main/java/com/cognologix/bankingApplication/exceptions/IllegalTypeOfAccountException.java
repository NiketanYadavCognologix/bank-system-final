package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class IllegalTypeOfAccountException extends RuntimeException {
    private Integer code;
    private String Message;

    public IllegalTypeOfAccountException() {
    }

    public IllegalTypeOfAccountException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForCustomer.DUPLICATE_CUSTOMER_ID.getCode();
    }
}
