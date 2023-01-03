package com.cognologix.bankingApplication.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogMessages {
    SUCCESSFUL_REQUEST("Request send successfully"),
    SUCCESSFUL_UPDATE_DATABASE("Database updated successfully"),
    SUCCESSFUL_RESPONSE_FROM_SERVICE("Successfully returning from service"),

    SUCCESSFUL_RESPONSE("Successfully getting response in controller");

    private String message;
}
