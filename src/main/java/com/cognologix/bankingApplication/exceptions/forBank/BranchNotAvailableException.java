package com.cognologix.bankingApplication.exceptions.forBank;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForBank;

public class BranchNotAvailableException extends RuntimeException{
    private Integer code;
    ErrorsForBank branchNotFound;

    public BranchNotAvailableException() {
    }

    public BranchNotAvailableException(String message) {
        super(message);
    }

    public BranchNotAvailableException(ErrorsForBank branchNotFound) {
        super(branchNotFound.toString());
        this.code = branchNotFound.getCode();
        this.branchNotFound = branchNotFound;
    }
}
