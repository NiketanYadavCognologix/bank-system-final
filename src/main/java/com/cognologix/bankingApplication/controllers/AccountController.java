package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.enums.LogMessages;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/account")

public class AccountController {

    private static final Logger LOGGER = LogManager.getLogger(AccountController.class);
    //bank side operations
    @Autowired
    private AccountService bankOperationsService;

    //creating new account by giving account DTO
    @PostMapping(value = "/create")
    public ResponseEntity<CreatedAccountResponse> createNewAccount(@Valid @RequestBody AccountDto accountDto) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final CreatedAccountResponse createdAccountResponse = bankOperationsService.createAccount(accountDto);
        HttpStatus httpStatus = createdAccountResponse.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(createdAccountResponse, httpStatus);

    }

    //deactivating given account by account number
    @PutMapping("/deactivate")
    public ResponseEntity<DeactivateAccountResponse> deactivateAccountByAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final DeactivateAccountResponse deactivateAccountResponse = bankOperationsService.deactivateAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus = deactivateAccountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        LOGGER.info(ForAccount.DEACTIVATE_ACCOUNT.getMessage() + " in controller");
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(deactivateAccountResponse, httpStatus);
    }

    //activating given account by account number
    @PutMapping("/activate")
    public ResponseEntity<ActivateAccountResponse> activateAccountBYAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final ActivateAccountResponse activateAccountResponse = bankOperationsService.activateAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus = activateAccountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(activateAccountResponse, httpStatus);
    }

    //get all deactivated accounts
    @GetMapping("/get-deactivated-accounts")
    public ResponseEntity<DeactivatedAccountsResponse> getListOfDeactivatingAccounts() {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final DeactivatedAccountsResponse deactivatedAccounts = bankOperationsService.getAllDeactivatedAccounts();
        HttpStatus httpStatus = deactivatedAccounts.getSuccess() ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(bankOperationsService.getAllDeactivatedAccounts(), HttpStatus.OK);
    }

    //deposit amount to the given account number
    @PutMapping(value = "/deposit-amount")
    public ResponseEntity<DepositAmountResponse> depositAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final DepositAmountResponse depositAmountResponse = bankOperationsService.depositAmount(accountNumber, amount);
        HttpStatus httpStatus = depositAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(depositAmountResponse, httpStatus);
    }

    //withdraw amount to the given account number
    @PutMapping(value = "/withdraw-amount")
    public ResponseEntity<WithdrawAmountResponse> withdrawAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final WithdrawAmountResponse withdrawAmountResponse = bankOperationsService.withdrawAmount(accountNumber, amount);
        HttpStatus httpStatus = withdrawAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(withdrawAmountResponse, httpStatus);
    }

    //transferring amount from one account to another account
    @PutMapping("/transfer-amount")
    public ResponseEntity<TransferAmountResponse> moneyTransfer(@PathParam(value = "senderAccountNumber") Long senderAccountNumber, @PathParam(value = "receiverAccountNumber") Long receiverAccountNumber, @PathParam(value = "amount") Double amount) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final TransferAmountResponse transferAmountResponse = bankOperationsService.moneyTransfer(senderAccountNumber, receiverAccountNumber, amount);
        HttpStatus httpStatus = transferAmountResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-4");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(transferAmountResponse, httpStatus);
    }

    //checking balance by giving account number
    @GetMapping("/balance-inquiry")
    public ResponseEntity<BalanceInquiryResponse> checkBalance(@Valid @RequestParam(value = "accountNumber") Long accountNumber) {
        ThreadContext.put("executionStep","step-1");
        LOGGER.info(LogMessages.SUCCESSFUL_REQUEST.getMessage());
        final BalanceInquiryResponse balanceInquiryResponse = bankOperationsService.getAccountBalance(accountNumber);
        HttpStatus httpStatus = balanceInquiryResponse.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ThreadContext.put("executionStep","step-3");
        LOGGER.info(LogMessages.SUCCESSFUL_RESPONSE.getMessage());
        return new ResponseEntity<>(balanceInquiryResponse, httpStatus);
    }
}
