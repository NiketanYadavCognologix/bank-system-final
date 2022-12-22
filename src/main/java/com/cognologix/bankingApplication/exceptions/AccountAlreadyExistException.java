package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountAlreadyExistException extends RuntimeException {
    private Integer code;
    private String Message;

    public AccountAlreadyExistException() {
    }

    public AccountAlreadyExistException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForAccount.ACCOUNT_ALREADY_EXIST.getCode();
    }
}
