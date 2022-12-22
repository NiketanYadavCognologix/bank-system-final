package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Data;

@Data
public class InsufficientBalanceException extends RuntimeException {
    private Integer code;
    private String Message;

    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForAccount.INSUFFICIENT_BALANCE.getCode();
    }
}
