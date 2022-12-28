package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class DeactivateAccountException extends RuntimeException {
    private Integer code;
    private ErrorsForAccount deactivateAccount;

    public DeactivateAccountException() {
    }

    public DeactivateAccountException(String message) {
        super(message);
    }

    public DeactivateAccountException(ErrorsForAccount deactivateAccount) {
        super(deactivateAccount.toString());
        this.code = deactivateAccount.getCode();
        this.deactivateAccount = deactivateAccount;
    }
}
