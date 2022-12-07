package com.cognologix.bankingApplication.exceptions;

public class AccountAlreadyActivatedException extends RuntimeException{
    public AccountAlreadyActivatedException() {
    }

    public AccountAlreadyActivatedException(String message) {
        super(message);
    }
}
