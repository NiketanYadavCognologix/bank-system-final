package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountNotAvailableException extends RuntimeException {
    private Integer code;
    private String Message;

    public AccountNotAvailableException() {
    }

    public AccountNotAvailableException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.getCode();
    }

}
