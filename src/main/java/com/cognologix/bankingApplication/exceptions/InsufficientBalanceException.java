package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Data;

@Data
public class InsufficientBalanceException extends RuntimeException {
    private Integer code;
    private ErrorsForAccount insufficientBalance;
    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(ErrorsForAccount insufficientBalance) {
        super(insufficientBalance.toString());
        this.code = insufficientBalance.getCode();
        this.insufficientBalance =insufficientBalance;
    }
}
