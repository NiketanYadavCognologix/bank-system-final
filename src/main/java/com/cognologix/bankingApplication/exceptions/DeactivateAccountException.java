package com.cognologix.bankingApplication.exceptions;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import lombok.Getter;

@Getter
public class DeactivateAccountException extends RuntimeException {
    private Integer code;
    private String Message;

    public DeactivateAccountException() {
    }

    public DeactivateAccountException(String message) {
        super(message);
        this.Message = message;
        this.code = ErrorsForAccount.DEACTIVATE_ACCOUNT.getCode();
    }
}
