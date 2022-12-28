package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class DuplicateCustomerIDException extends RuntimeException {
    private Integer code;
    private String Message;
    ErrorsForCustomer errorsForCustomer;

    public DuplicateCustomerIDException() {
    }

    public DuplicateCustomerIDException(String message) {
        super(message);
        this.code = ErrorsForCustomer.DUPLICATE_CUSTOMER_ID.getCode();
    }

    public DuplicateCustomerIDException(ErrorsForCustomer duplicateCustomer) {
        super(duplicateCustomer.getMessage());
        this.Message = duplicateCustomer.getMessage();
        this.errorsForCustomer = duplicateCustomer;
    }
}
