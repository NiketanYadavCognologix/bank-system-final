package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;

public class AccountNotAvailableException extends RuntimeException{
    ErrorsForAccount errorsForAccount;
    public AccountNotAvailableException() {
    }

    public AccountNotAvailableException(String message) {
        super(message);
    }

    public AccountNotAvailableException(ErrorsForAccount accountNotAvailable) {
        errorsForAccount=accountNotAvailable;
    }
}
