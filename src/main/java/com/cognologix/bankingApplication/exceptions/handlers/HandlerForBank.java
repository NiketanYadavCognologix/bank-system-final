package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForBank;
import com.cognologix.bankingApplication.exceptions.forBank.BankNameNotFoundException;
import com.cognologix.bankingApplication.exceptions.forBank.BranchAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.forBank.BranchNotAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerForBank {

    private static final Logger LOGGER = LogManager.getLogger(HandlerForBank.class);
    @ExceptionHandler({
            BankNameNotFoundException.class,
            BranchAlreadyExistException.class,
            BranchNotAvailableException.class
    })
    public ResponseEntity<ApplicationError> handleBankException(Exception exception){
        Integer code = ErrorsForBank.valueOf(exception.getMessage()).getCode();
        String message = ErrorsForBank.valueOf(exception.getMessage()).getMessage();
        ApplicationError applicationError = new ApplicationError(code, message);
        LOGGER.error("Exception : {} : "+message, exception.getStackTrace());
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }
}
