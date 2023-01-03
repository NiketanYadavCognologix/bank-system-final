package com.cognologix.bankingApplication.exceptions.forBank;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForBank;
import lombok.Getter;

@Getter
public class BankNameNotFoundException extends RuntimeException{
    private Integer code;
    ErrorsForBank bankNameNotFound;

    public BankNameNotFoundException() {
    }

    public BankNameNotFoundException(String message) {
        super(message);
    }

    public BankNameNotFoundException(ErrorsForBank bankNameNotFound) {
        super(bankNameNotFound.toString());
        this.code = bankNameNotFound.getCode();
        this.bankNameNotFound = bankNameNotFound;
    }
}
