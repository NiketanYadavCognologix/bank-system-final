package com.cognologix.bankingApplication.controllers;

import java.util.List;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.entities.Account;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cognologix.bankingApplication.services.BankOperationsSevice;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/banking")
public class BankAccountOperationsController {

    //bank side operations
    @Autowired
    BankOperationsSevice bankOperationsSevice;
    JSONObject resultSet;

    //creating new account by givng account DTO
    @PostMapping("/createAccount")
    public ResponseEntity<Account> createNewAccount(@Valid @RequestBody AccountDto accountDto) {
        Account createdAccount = bankOperationsSevice.saveObject(accountDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);

    }

    //delete accounts based on customerId
    @DeleteMapping("/deleteCustomerById")
    public ResponseEntity<?> deleteCustomerById(@PathParam(value = "id") Integer id) {
        List<Account> deletedAccounts = bankOperationsSevice.deleteCustomer(id);
        resultSet = new JSONObject();
        resultSet.put("Customer deleted successfully....", deletedAccounts);
        return new ResponseEntity<>(resultSet, HttpStatus.OK);
    }

    //deposit amount based on account number
    @PutMapping(value = "/deposit")
    public ResponseEntity<?> depositAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsSevice.deposit(accountNumber, amount);
        return new ResponseEntity<String>("Amount deposit successfully...", HttpStatus.OK);
    }

    //withdraw amount to the given account number
    @PutMapping(value = "/withdraw")
    public ResponseEntity<?> withdrawAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsSevice.withdraw(accountNumber, amount);
        return new ResponseEntity<String>("Amount withdrawn successfully...", HttpStatus.OK);
    }

    //return accounts list of single customer by id
    @GetMapping("/getAllByCustomerID/{customerId}")
    public ResponseEntity<?> getAllAccounts(@PathVariable Integer customerId) {
        List<Account> accounts = bankOperationsSevice.getAllAccountsForCustomers(customerId);
        return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
    }


    //transferring amount from one account to another account
    @PutMapping("/transfer")
    public ResponseEntity<?> moneyTransfer(@PathParam(value = "senderAccountNumber") Long senderAccountNumber, @PathParam(value = "receiverAccountNumber") Long receiverAccountNumber, @PathParam(value = "amount") Double amount) {
        bankOperationsSevice.moneyTransfer(senderAccountNumber, receiverAccountNumber, amount);
        return new ResponseEntity<String>("Amount successfully transfer...", HttpStatus.OK);
    }
//	public Customer updateInformation(Customer customerForUpdate);
//
//	public Customer showCustomerDetails(Integer accountId);
}
