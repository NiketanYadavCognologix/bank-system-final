package com.cognologix.bankingApplication.exceptions.handlers;

import lombok.Data;

@Data
public class ApplicationError {

    private Integer code;
    private String message;
}
