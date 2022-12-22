package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountAlreadyActivatedException extends RuntimeException {

    private Integer code;
    private String Message;

    public AccountAlreadyActivatedException() {
    }

    public AccountAlreadyActivatedException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForAccount.ACCOUNT_ALREADY_ACTIVATE.getCode();


    }

}
