package com.cognologix.bankingApplication.exceptions;

public class IllegalTypeOfAccountException extends RuntimeException{
    public IllegalTypeOfAccountException() {
    }

    public IllegalTypeOfAccountException(String message) {
        super(message);
    }
}
