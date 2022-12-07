package com.cognologix.bankingApplication.exceptions;

public class AccountAlreadyDeactivatedException extends RuntimeException{
    public AccountAlreadyDeactivatedException() {
    }

    public AccountAlreadyDeactivatedException(String message) {
        super(message);
    }
}
