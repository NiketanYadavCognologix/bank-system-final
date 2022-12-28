package com.cognologix.bankingApplication.enums.errorWithErrorCode;

import lombok.Getter;

@Getter
public enum ErrorsForAccount {


    ACCOUNT_ALREADY_ACTIVATE(900, "Account already activated"),

    ACCOUNT_ALREADY_DEACTIVATE(901, "Already deactivated"),

    ACCOUNT_ALREADY_EXIST(902, "Account already exist"),

    ACCOUNT_NOT_AVAILABLE(903, "Account not available"),

    DEACTIVATE_ACCOUNT(904, "Inactive account"),

    NO_ANY_DEACTIVATED_ACCOUNT_FOUND(908, "No any deactivated account found"),

    ILLEGAL_TYPE_OF_ACCOUNT(905, "Invalid account type"),

    INSUFFICIENT_BALANCE(906, "Insufficient balance"),

    INACTIVE_AMOUNT_RECEIVER_ACCOUNT(907, "inactive receiver account");

    private final Integer code;
    private final String message;

    ErrorsForAccount(Integer code, String message) {
        this.code = code;
        this.message = message;

    }


}
