package com.cognologix.bankingApplication.exceptions.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
