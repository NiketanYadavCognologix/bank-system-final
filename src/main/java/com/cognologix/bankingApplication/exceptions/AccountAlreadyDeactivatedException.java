package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountAlreadyDeactivatedException extends RuntimeException {
    private Integer code;
    private ErrorsForAccount accountAlreadyDeactivate;

    public AccountAlreadyDeactivatedException() {
    }

    public AccountAlreadyDeactivatedException(String message) {
        super(message);
    }

    public AccountAlreadyDeactivatedException(ErrorsForAccount accountAlreadyDeactivate) {
        super(accountAlreadyDeactivate.toString());
        this.code = accountAlreadyDeactivate.getCode();
        this.accountAlreadyDeactivate = accountAlreadyDeactivate;
    }
}
