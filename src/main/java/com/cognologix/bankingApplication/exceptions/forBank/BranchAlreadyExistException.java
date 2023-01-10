package com.cognologix.bankingApplication.exceptions.forBank;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForBank;
import lombok.Getter;

@Getter
public class BranchAlreadyExistException extends RuntimeException{
    private Integer code;
    ErrorsForBank branchAlreadyExist;

    public BranchAlreadyExistException() {
    }

    public BranchAlreadyExistException(String message) {
        super(message);
    }

    public BranchAlreadyExistException(ErrorsForBank branchAlreadyExist) {
        super(branchAlreadyExist.toString());
        this.code = branchAlreadyExist.getCode();
        this.branchAlreadyExist = branchAlreadyExist;
    }
}
