package com.cognologix.bankingApplication.enums.responseMessages;

public enum ForAccount {
    CREATE_ACCOUNT("Account created successfully"),

    DEPOSIT_AMOUNT(" deposited successfully"),

    WITHDRAW_AMOUNT(" withdraw successfully"),

    TRANSFER_AMOUNT(" transferred successfully Remaining balance : "),

    AVAILABLE_BALANCE("Your account balance is : "),

    DEACTIVATE_ACCOUNT("Successfully deactivated "),

    ACTIVATED_ACCOUNT("Successfully activated "),

    LIST_OF_DEACTIVATED_ACCOUNTS("Deactivated accounts are");

    private String message;
    ForAccount(String message)
    {
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

}
