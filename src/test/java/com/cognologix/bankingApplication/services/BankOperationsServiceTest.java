package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BankOperationsServiceTest {


//    @Autowired
//    BankAccountRepository bankAccountRepository;
//
//    @Autowired
//    CustomerRepository customerRepository;
//
//    @Autowired
//    private BankOperationsService bankOperationsSevice;
//
//    @Autowired
//    private CustomerOperationService customerOperationService;
//
//    Customer customer = new Customer(1, "Onkar", LocalDate.of(1998, 11, 11),
//            "903997989010", "PAK36SURYA", "sury6awanshi@gmail.com", "Male");
//
//    AccountDto accountDto = new AccountDto(3, "Current", 1000.0, 1);
//
//    Account account = new Account(accountDto.getAccountID(), "Active", accountDto.getAccountType(),
//            1001L, accountDto.getBalance(), customer);
//
//    @Test
//    @Ignore
//    void createAccount() {
//        CreatedAccountResponse actualResponse = bankOperationsSevice.createAccount(accountDto);
//        CreatedAccountResponse expectedResponse = new CreatedAccountResponse(true, "Account created successfully...", account);
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void getAccountByAccountNumber() {
//        Account actualResponse = bankOperationsSevice.getAccountByAccountNumber(1001L);
//        Account expectedResponse = account;
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void deposit() {
//        Double updatedBalance = bankOperationsSevice.getAccountByAccountNumber(1000L).getBalance() + 1200.2;
//        DepositAmountResponse expectedResponse = new DepositAmountResponse(true,
//                1200.2 + " deposited successfully... \nAvailable balance is : " + updatedBalance);
//        DepositAmountResponse actualResponse = bankOperationsSevice.deposit(1000L, 1200.2);
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void withdraw() {
//        Double updatedBalance = bankOperationsSevice.getAccountByAccountNumber(1000L).getBalance() - 1200.2;
//        WithdrawAmountResponse expectedResponse = new WithdrawAmountResponse(true,
//                1200.2 + " withdraw successfully... \nAvailable balance is : " + updatedBalance);
//        WithdrawAmountResponse actualResponse = bankOperationsSevice.withdraw(1000L, 1200.2);
//        assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    void moneyTransfer() {
//
//        Double updatedBalance = bankOperationsSevice.getAccountByAccountNumber(1000L).getBalance() - 200;
//        TransferAmountResponse expectedResponse = new TransferAmountResponse(true,
//                "Amount " + 200.00 + " transferred successfully... \nRemaining balance is " + updatedBalance);
//
//        TransferAmountResponse actualResponse = bankOperationsSevice.moneyTransfer(1000L, 1001L, 200.00);
//        assertEquals(expectedResponse, actualResponse);
//
//    }

}