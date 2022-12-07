package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerNotFoundExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> hanleCustomerNotFoundException(Exception ex){
        return new ResponseEntity<>("Exception : "+ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
