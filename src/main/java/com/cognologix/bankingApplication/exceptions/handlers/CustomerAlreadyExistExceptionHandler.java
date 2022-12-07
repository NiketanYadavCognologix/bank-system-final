package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerAlreadyExistExceptionHandler {
    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<String> handleCustomerAlreadyExistException(Exception exception){
        return new ResponseEntity<>("Message : "+exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
