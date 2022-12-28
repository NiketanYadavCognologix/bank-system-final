package com.cognologix.bankingApplication.enums.errorWithErrorCode;

import lombok.Getter;

@Getter
public enum ErrorsForCustomer {
    CUSTOMER_ALREADY_EXIST(801,"customer already exist by adhar number, pan number OR email id"),

    CUSTOMER_NOT_FOUND(802,"customer not found"),

    DUPLICATE_CUSTOMER_ID(803,"Duplicate customer id"),

    ACCOUNT_NOT_AVAILABLE(804,"No account found for this customer");
    private final Integer code;
    private final String message;
    ErrorsForCustomer(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
