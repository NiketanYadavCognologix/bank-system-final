package com.cognologix.bankingApplication.exceptions.handlers;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApplicationError {

    private Timestamp timestamp;
    private Integer code;
    private String message;

    public ApplicationError() {

    }

    public ApplicationError(Integer code, String message) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.code = code;
        this.message = message;
    }
}
