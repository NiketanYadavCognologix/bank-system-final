package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Data;

@Data
public class InsufficientBalanceException extends RuntimeException {
    private Integer code;
    private String Message;

    private ErrorsForAccount errorsForAccount;
    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
        this.code = ErrorsForAccount.INSUFFICIENT_BALANCE.getCode();
    }

    public InsufficientBalanceException(ErrorsForAccount insufficientBalance) {
        super(insufficientBalance.getMessage());
        this.Message = insufficientBalance.getMessage();
        this.errorsForAccount=insufficientBalance;
    }
}
