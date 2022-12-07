package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.exceptions.IllegalTypeOfAccountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IllegalTypeOfAccountExceptionHandler {
    @ExceptionHandler(IllegalTypeOfAccountException.class)
    public ResponseEntity<String> handleIllegalTypeOfAccountException(Exception exception){
        return new ResponseEntity<>("Exception : "+exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
