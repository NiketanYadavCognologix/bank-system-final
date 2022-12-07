package com.cognologix.bankingApplication.exceptions;

public class DeactivateAccountException extends RuntimeException{
    public DeactivateAccountException() {
    }

    public DeactivateAccountException(String message) {
        super(message);
    }
}
