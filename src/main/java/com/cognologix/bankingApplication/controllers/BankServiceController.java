package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cognologix.bankingApplication.services.BankOperationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/banking")
public class BankServiceController {

    //bank side operations
    @Autowired
    BankOperationsService bankOperationsService;

    //creating new account by giving account DTO
    @PostMapping(value = "/createAccount", consumes = {"application/json", "application/xml"})
    public ResponseEntity<CreatedAccountResponse> createNewAccount(@Valid @RequestBody AccountDto accountDto) {
        CreatedAccountResponse createdAccountResponse = bankOperationsService.createAccount(accountDto);
        HttpStatus httpStatus = createdAccountResponse.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(createdAccountResponse, httpStatus);
    }

    //deactivating given account by account number
    @PutMapping("/deactivateAccount")
    public ResponseEntity<DeactivateAccountResponse> deactivateAccountByAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        DeactivateAccountResponse deactivateAccountResponse = bankOperationsService.deactivateAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus = deactivateAccountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(deactivateAccountResponse, httpStatus);
    }

    //activating given account by account number
    @PutMapping("/activateAccount")
    public ResponseEntity<ActivateAccountResponse> activateAccountBYAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        ActivateAccountResponse activateAccountResponse = bankOperationsService.activateAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus = activateAccountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(activateAccountResponse, httpStatus);
    }

    //get all deactivated accounts
    @GetMapping("/getDeactivatingAccounts")
    public ResponseEntity<DeactivatedAccountsResponse> getListOfDeactivatingAccounts() {
        DeactivatedAccountsResponse deactivatedAccounts = bankOperationsService.getAllDeactivatedAccounts();
        HttpStatus httpStatus = deactivatedAccounts.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(bankOperationsService.getAllDeactivatedAccounts(), HttpStatus.OK);
    }

    //deposit amount to the given account number
    @PutMapping(value = "/deposit")
    public ResponseEntity<DepositAmountResponse> depositAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        DepositAmountResponse depositAmountResponse = bankOperationsService.deposit(accountNumber, amount);
        HttpStatus httpStatus = depositAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(depositAmountResponse, httpStatus);
    }

    //withdraw amount to the given account number
    @PutMapping(value = "/withdraw")
    public ResponseEntity<WithdrawAmountResponse> withdrawAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        WithdrawAmountResponse withdrawAmountResponse = bankOperationsService.withdraw(accountNumber, amount);
        HttpStatus httpStatus = withdrawAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(withdrawAmountResponse, httpStatus);
    }

    //transferring amount from one account to another account
    @PutMapping("/transfer")
    public ResponseEntity<TransferAmountResponse> moneyTransfer(@PathParam(value = "senderAccountNumber") Long senderAccountNumber, @PathParam(value = "receiverAccountNumber") Long receiverAccountNumber, @PathParam(value = "amount") Double amount) {
        TransferAmountResponse transferAmountResponse = bankOperationsService.moneyTransfer(senderAccountNumber, receiverAccountNumber, amount);
        HttpStatus httpStatus = transferAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(transferAmountResponse, httpStatus);
    }
}
