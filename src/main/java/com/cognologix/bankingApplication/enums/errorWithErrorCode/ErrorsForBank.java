package com.cognologix.bankingApplication.enums.errorWithErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorsForBank {
    BANK_NAME_NOT_FOUND(1001,"Bank for given bank name not exist"),

    BRANCH_ALREADY_EXIST(1002,"Branch already exist"),

    BRANCH_NOT_FOUND(1003,"Branch not found");
    private Integer code;
    private String message;
}
