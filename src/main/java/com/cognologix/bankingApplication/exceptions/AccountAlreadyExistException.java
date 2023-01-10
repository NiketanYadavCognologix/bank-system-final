package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class AccountAlreadyExistException extends RuntimeException {
    private Integer code;
    private ErrorsForAccount accountAlreadyExist;

    public AccountAlreadyExistException() {
    }

    public AccountAlreadyExistException(String message) {
        super(message);
    }

    public AccountAlreadyExistException(ErrorsForAccount accountAlreadyExist) {
        super(accountAlreadyExist.toString());
        this.code = accountAlreadyExist.getCode();
        this.accountAlreadyExist = accountAlreadyExist;
    }
}
