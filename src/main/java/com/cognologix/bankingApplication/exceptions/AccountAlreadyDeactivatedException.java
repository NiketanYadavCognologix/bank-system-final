package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountAlreadyDeactivatedException extends RuntimeException {
    private Integer code;
    private String Message;

    public AccountAlreadyDeactivatedException() {
    }

    public AccountAlreadyDeactivatedException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForAccount.ACCOUNT_ALREADY_DEACTIVATE.getCode();

    }
}
