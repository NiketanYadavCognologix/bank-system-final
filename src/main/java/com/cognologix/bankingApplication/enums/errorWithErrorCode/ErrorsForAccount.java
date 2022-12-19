package com.cognologix.bankingApplication.enums.errorWithErrorCode;

public enum ErrorsForAccount {


    accountAlreadyActivate(900,"Account already activated"),

    accountAlreadyDeactivate(901,"Already deactivated"),

    accountAlreadyExist(902,"Account already exist"),

    accountNotAvailable(903,"Account not available"),

    deactivateAccount(904,"Inactive account"),

    illegalTypeOFAccount(905,"Invalid account type"),

    insufficientBalance(906,"Insufficient balance"),

    inactiveAmountReceiverAccount(907,"inactive receiver account");

    private final Integer code;
    private final String message;
    ErrorsForAccount(Integer code, String message) {
        this.code = code;
        this.message = message;

    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
