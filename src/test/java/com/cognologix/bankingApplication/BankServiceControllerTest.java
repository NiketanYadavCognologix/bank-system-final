package com.cognologix.bankingApplication;

import com.cognologix.bankingApplication.controllers.BankAccountOperationsController;
import com.cognologix.bankingApplication.globleObjectLists.DataSoucrce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BankServiceControllerTest {

    @Autowired
    private DataSoucrce dataSoucrce;

    @Autowired
    BankAccountOperationsController bankServiceController;

    @Test
    public void testCreateAccout() {
        System.out.println("Testing object get created.....");
        //actual output
//        AccountDto accountDto = new AccountDto(1, "Savings", 1000.0, 1);
//        ResponseEntity<Account> createdAccount = bankServiceController.createNewAccount(accountDto);

        //expected output
//        ResponseEntity<Account> excpectedOutput = new ResponseEntity<>();
//        excpectedOutput.
//      Integer resultTocheck = bankOperationsSevice.createAccount(customer1);
//      Integer resultGetting= bankOperationsSevice.createAccount();
    }
}
