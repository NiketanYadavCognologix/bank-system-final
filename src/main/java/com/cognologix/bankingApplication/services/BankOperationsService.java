package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
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

    DepositAmountResponse deposit(Long accountNumber, Double amount);

    WithdrawAmountResponse withdraw(Long accountNumber, Double amount);

    TransferAmountResponse moneyTransfer(Long accountNumberWhoSendMoney,Long accountNumberWhoReceiveMoney,Double amountForTransfer);

    TransactionStatementResponse transactionsOfAccount(Long fromAccountNumber);

    DeactivateAccountResponse deactivateAccountByAccountNumber(Long accountNumber);

    ActivateAccountResponse activateAccountByAccountNumber(Long accountNumber);

    DeactivatedAccountsResponse getAllDeactivatedAccounts();
}
