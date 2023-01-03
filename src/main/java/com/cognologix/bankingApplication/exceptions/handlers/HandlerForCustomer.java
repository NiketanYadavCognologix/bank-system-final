package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.exceptions.DuplicateCustomerIDException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerForCustomer {

    private static final Logger LOGGER = LogManager.getLogger(HandlerForCustomer.class);

    @ExceptionHandler({
            CustomerAlreadyExistException.class,
            CustomerNotFoundException.class,
            DuplicateCustomerIDException.class,
    })
    public ResponseEntity<ApplicationError> handleCustomerException(Exception exception) {
        Integer code = ErrorsForCustomer.valueOf(exception.getMessage()).getCode();
        String message = ErrorsForCustomer.valueOf(exception.getMessage()).getMessage();
        ApplicationError applicationError = new ApplicationError(code, message);
        LOGGER.error("Exception : {} : "+message, exception.getStackTrace());
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }

}
