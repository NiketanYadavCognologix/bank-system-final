package com.cognologix.bankingApplication.enums.responseMessages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ForBank {

    NEW_BRANCH("Branch created successfully");
    private String message;
}
