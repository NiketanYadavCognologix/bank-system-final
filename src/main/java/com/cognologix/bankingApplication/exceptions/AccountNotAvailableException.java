package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class AccountNotAvailableException extends RuntimeException {
    private Integer code;
    private ErrorsForAccount errorsForAccount;
    private ErrorsForCustomer errorsForCustomer;
    public AccountNotAvailableException() {
    }

    public AccountNotAvailableException(String message) {
        super(message);
        this.code = ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.getCode();
    }

    public AccountNotAvailableException(ErrorsForCustomer accountNotAvailable) {
        super(accountNotAvailable.toString());
        this.errorsForCustomer=accountNotAvailable;
    }

    public AccountNotAvailableException(ErrorsForAccount accountNotAvailable) {
        super(accountNotAvailable.toString());
        this.errorsForAccount=accountNotAvailable;
    }
}
