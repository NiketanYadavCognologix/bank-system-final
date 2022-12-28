package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import lombok.Getter;

@Getter
public class IllegalTypeOfAccountException extends RuntimeException {
    private Integer code;

    private ErrorsForAccount illegalTypeOfAccount;
    public IllegalTypeOfAccountException() {
    }

    public IllegalTypeOfAccountException(String message) {
        super(message);
    }

    public IllegalTypeOfAccountException(ErrorsForAccount illegalTypeOfAccount) {
        super(illegalTypeOfAccount.toString());
        this.code = illegalTypeOfAccount.getCode();
        this.illegalTypeOfAccount=illegalTypeOfAccount;
    }
}
