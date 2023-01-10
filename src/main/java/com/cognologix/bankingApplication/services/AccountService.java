package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.entities.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

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
