package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
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
            DeactivateAccountException.class,
            InsufficientBalanceException.class,
            IllegalTypeOfAccountException.class,
            AccountAlreadyExistException.class
    })
    public ResponseEntity<ApplicationError> handleAccountException(Exception exception) {
        Integer code = ErrorsForAccount.valueOf(exception.getMessage()).getCode();
        String message = ErrorsForAccount.valueOf(exception.getMessage()).getMessage();
        ApplicationError applicationError = new ApplicationError(code, message);
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CustomerAlreadyExistException.class,
            CustomerNotFoundException.class,
            DuplicateCustomerIDException.class,
    })
    public ResponseEntity<ApplicationError> handleCustomerException(Exception exception) {
        Integer code = ErrorsForCustomer.valueOf(exception.getMessage()).getCode();
        String message = ErrorsForCustomer.valueOf(exception.getMessage()).getMessage();
        ApplicationError applicationError = new ApplicationError(code, message);
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }
}
