package com.cognologix.bankingApplication.enums.responseMessages;

public enum ForCustomer {
    CREATE_CUSTOMER("Customer created successfully"),
    STATEMENT("Successfully get bank statement"),
    ALL_ACCOUNTS_FOR_CUSTOMER("Found accounts "),
    UPDATE_CUSTOMER("Updated successfully");


    private String message;
    ForCustomer(String message)
    {
        this.message=message;
    }
    public String getMessage() {
        return message;
    }
}
