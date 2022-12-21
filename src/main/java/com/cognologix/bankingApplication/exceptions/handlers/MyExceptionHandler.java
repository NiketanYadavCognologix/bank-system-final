package com.cognologix.bankingApplication.exceptions.handlers;

import com.cognologix.bankingApplication.exceptions.*;
import org.json.JSONObject;
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
    public ResponseEntity<JSONObject> handleIllegalTypeOfAccountException(Exception exception) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Exception : ", exception.getMessage());
        return new ResponseEntity<>(jsonObject, HttpStatus.BAD_REQUEST);
    }
}
