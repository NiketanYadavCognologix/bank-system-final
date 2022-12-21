package com.cognologix.bankingApplication.enums.errorWithErrorCode;

public enum ErrorsForCustomer {
    CUSTOMER_ALREADY_EXIST(801,"customer already exist by adhar number, pan number OR email id"),

    CUSTOMER_NOT_FOUND(802,"customer not found"),

    DUPLICATE_CUSTOMER_ID(803,"Duplicate customer id");
    private final Integer code;
    private final String message;
    ErrorsForCustomer(Integer code, String message) {
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
