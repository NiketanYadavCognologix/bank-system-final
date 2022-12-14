package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.entities.Account;

public interface BankOperationsService {

    CreatedAccountResponse createAccount(AccountDto accountDto);

    Account getAccountByAccountNumber(Long accountId);

    DepositAmountResponse depositAmount(Long accountNumber, Double amount);

    WithdrawAmountResponse withdrawAmount(Long accountNumber, Double amount);

    TransferAmountResponse moneyTransfer(Long accountNumberWhoSendMoney,Long accountNumberWhoReceiveMoney,Double amountForTransfer);

    //get account balance by account number
    BalanceInquiryResponse getAccountBalance(Long accountNumber);

    TransactionStatementResponse transactionsOfAccount(Long fromAccountNumber);

    DeactivateAccountResponse deactivateAccountByAccountNumber(Long accountNumber);

    ActivateAccountResponse activateAccountByAccountNumber(Long accountNumber);

    DeactivatedAccountsResponse getAllDeactivatedAccounts();
}
