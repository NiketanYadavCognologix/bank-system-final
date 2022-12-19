package com.cognologix.bankingApplication.enums.responseMessages;

public enum ForAccount {
    CreateAccount("Account created successfully"),

    depositAmount(" deposited successfully"),

    withdrawAmount(" withdraw successfully"),

    transferAmount(" transferred successfully Remaining balance : "),

    availableBalance("Your account balance is : "),

    deactivateAccount("Successfully deactivated "),

    activatedAccount("Successfully activated "),

    listOfDeactivatedAccounts("Deactivated accounts are");
    ForAccount(String message) {
    }

}
