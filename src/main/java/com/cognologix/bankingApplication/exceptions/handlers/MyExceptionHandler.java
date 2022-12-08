package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.exceptions.AccountAlreadyActivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyDeactivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.exceptions.DeactivateAccountException;
import com.cognologix.bankingApplication.exceptions.DuplicateCustomerIDException;
import com.cognologix.bankingApplication.exceptions.IllegalTypeOfAccountException;
import com.cognologix.bankingApplication.exceptions.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler({
            AccountAlreadyActivatedException.class,
            AccountAlreadyDeactivatedException.class,
            AccountNotAvailableException.class,
            CustomerAlreadyExistException.class,
            CustomerNotFoundException.class,
            DeactivateAccountException.class,
            DuplicateCustomerIDException.class,
            InsufficientBalanceException.class,
            IllegalTypeOfAccountException.class,
            AccountAlreadyExistException.class,
                })
    public ResponseEntity<String> handleIllegalTypeOfAccountException(Exception exception){
        return new ResponseEntity<>("Exception : "+exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
