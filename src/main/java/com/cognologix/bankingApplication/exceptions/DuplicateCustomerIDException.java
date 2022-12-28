package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class DuplicateCustomerIDException extends RuntimeException {
    private Integer code;
    ErrorsForCustomer duplicateCustomer;

    public DuplicateCustomerIDException() {
    }

    public DuplicateCustomerIDException(String message) {
        super(message);
    }

    public DuplicateCustomerIDException(ErrorsForCustomer duplicateCustomer) {
        super(duplicateCustomer.toString());
        this.code = duplicateCustomer.getCode();
        this.duplicateCustomer = duplicateCustomer;
    }
}
