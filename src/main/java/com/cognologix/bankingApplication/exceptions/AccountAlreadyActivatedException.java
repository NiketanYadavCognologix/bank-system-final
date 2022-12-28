package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountAlreadyActivatedException extends RuntimeException {

    private Integer code;
    ErrorsForAccount accountAlreadyActivate;

    public AccountAlreadyActivatedException() {
    }

    public AccountAlreadyActivatedException(String message) {
        super(message);
    }

    public AccountAlreadyActivatedException(ErrorsForAccount accountAlreadyActivate) {
        super(accountAlreadyActivate.toString());
        this.code = accountAlreadyActivate.getCode();
        this.accountAlreadyActivate = accountAlreadyActivate;
    }
}
