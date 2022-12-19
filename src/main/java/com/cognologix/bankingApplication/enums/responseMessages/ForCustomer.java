package com.cognologix.bankingApplication.enums.responseMessages;

public enum ForCustomer {
    createCustomer("Created successfully"),

    allAccountsForCustomer("Found accounts "),
    updateCustomer("Updated successfully");

    ForCustomer(String message) {
    }
}
