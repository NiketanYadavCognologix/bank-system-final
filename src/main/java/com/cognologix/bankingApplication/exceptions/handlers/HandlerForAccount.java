package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyActivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyDeactivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.DeactivateAccountException;
import com.cognologix.bankingApplication.exceptions.IllegalTypeOfAccountException;
import com.cognologix.bankingApplication.exceptions.InsufficientBalanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerForAccount {

    private static final Logger LOGGER = LogManager.getLogger(HandlerForAccount.class);

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
        LOGGER.error("Exception : {} : "+message, exception.getStackTrace());
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }
}
