package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.services.BankOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/account")
public class BankServiceController {

    //bank side operations
    @Autowired
    private BankOperationsService bankOperationsService;

    //creating new account by giving account DTO
    @PostMapping(value = "/create")
    public ResponseEntity<CreatedAccountResponse> createNewAccount(@Valid @RequestBody AccountDto accountDto) {
        final CreatedAccountResponse createdAccountResponse = bankOperationsService.createAccount(accountDto);
        HttpStatus httpStatus = createdAccountResponse.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(createdAccountResponse, httpStatus);
    }

    //deactivating given account by account number
    @PutMapping("/deactivate")
    public ResponseEntity<DeactivateAccountResponse> deactivateAccountByAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        final DeactivateAccountResponse deactivateAccountResponse = bankOperationsService.deactivateAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus = deactivateAccountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(deactivateAccountResponse, httpStatus);
    }

    //activating given account by account number
    @PutMapping("/activate")
    public ResponseEntity<ActivateAccountResponse> activateAccountBYAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        final ActivateAccountResponse activateAccountResponse = bankOperationsService.activateAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus = activateAccountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(activateAccountResponse, httpStatus);
    }

    //get all deactivated accounts
    @GetMapping("/get-deactivated-accounts")
    public ResponseEntity<DeactivatedAccountsResponse> getListOfDeactivatingAccounts() {
        final DeactivatedAccountsResponse deactivatedAccounts = bankOperationsService.getAllDeactivatedAccounts();
        HttpStatus httpStatus = deactivatedAccounts.getSuccess() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(bankOperationsService.getAllDeactivatedAccounts(), HttpStatus.OK);
    }

    //deposit amount to the given account number
    @PutMapping(value = "/depositAmount")
    public ResponseEntity<DepositAmountResponse> depositAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        final DepositAmountResponse depositAmountResponse = bankOperationsService.depositAmount(accountNumber, amount);
        HttpStatus httpStatus = depositAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(depositAmountResponse, httpStatus);
    }

    //withdraw amount to the given account number
    @PutMapping(value = "/withdrawAmount")
    public ResponseEntity<WithdrawAmountResponse> withdrawAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        final WithdrawAmountResponse withdrawAmountResponse = bankOperationsService.withdrawAmount(accountNumber, amount);
        HttpStatus httpStatus = withdrawAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(withdrawAmountResponse, httpStatus);
    }

    //transferring amount from one account to another account
    @PutMapping("/transferAmount")
    public ResponseEntity<TransferAmountResponse> moneyTransfer(@PathParam(value = "senderAccountNumber") Long senderAccountNumber, @PathParam(value = "receiverAccountNumber") Long receiverAccountNumber, @PathParam(value = "amount") Double amount) {
        final TransferAmountResponse transferAmountResponse = bankOperationsService.moneyTransfer(senderAccountNumber, receiverAccountNumber, amount);
        HttpStatus httpStatus = transferAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(transferAmountResponse, httpStatus);
    }

    //checking balance by giving account number
    @GetMapping("/balanceInquiry")
    public ResponseEntity<BalanceInquiryResponse> checkBalance(@Valid @PathParam(value = "accountNumber") Long accountNumber) {
        final BalanceInquiryResponse balanceInquiryResponse = bankOperationsService.getAccountBalance(accountNumber);
        HttpStatus httpStatus = balanceInquiryResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(balanceInquiryResponse, httpStatus);
    }
}
