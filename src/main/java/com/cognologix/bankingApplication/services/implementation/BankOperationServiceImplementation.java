package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.dto.entityToDto.TransactionToTransactionDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.enums.AccountStatus;
import com.cognologix.bankingApplication.enums.AccountType;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyActivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyDeactivatedException;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.exceptions.DeactivateAccountException;
import com.cognologix.bankingApplication.exceptions.IllegalTypeOfAccountException;
import com.cognologix.bankingApplication.exceptions.InsufficientBalanceException;
import com.cognologix.bankingApplication.services.BankOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//service class for banking operations
@Service
public class BankOperationServiceImplementation implements BankOperationsService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //creating and saving account into database by JPA
    @Override
    public CreatedAccountResponse createAccount(AccountDto accountDto) {
        //new account
        try {
            String accountType;

            //check the account type is proper
            try {
                String accountTypeGivenByCustomer = AccountType.valueOf(accountDto.getAccountType().toUpperCase()).toString();
                accountType = accountTypeGivenByCustomer;
            } catch (Exception exception) {
                throw new IllegalTypeOfAccountException(ErrorsForAccount.ILLEGAL_TYPE_OF_ACCOUNT.getMessage());
            }

            //adding information from AccountDTO in account
            Customer customer = customerRepository.findByCustomerIdEquals(accountDto.getCustomerId());
            if (customer == null) {
                throw new CustomerNotFoundException(ErrorsForCustomer.CUSTOMER_NOT_FOUND.getMessage());
            }

            String status = AccountStatus.ACTIVATED.toString();
            Double balance = accountDto.getBalance();

            Account newAccount = new Account(null, status, accountType, balance, customer);
            bankAccountRepository.save(newAccount);

            //proper response after creating new account
            CreatedAccountResponse createdAccountResponse = new CreatedAccountResponse(true, ForAccount.CREATE_ACCOUNT.getMessage(), newAccount);

            //return the custom response
            return createdAccountResponse;

        } catch (IllegalTypeOfAccountException exception) {
            exception.printStackTrace();
            throw new IllegalTypeOfAccountException(exception.getMessage());
        } catch (CustomerNotFoundException exception) {
            exception.printStackTrace();
            throw new CustomerNotFoundException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    //get account by account number
    @Override
    public Account getAccountByAccountNumber(Long accountNumber) {
        try {
            Account account = foundedAccount(accountNumber);
            if (null == account) {
                throw new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.getMessage());
            }
            return account;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }

    //deposit amount in given account number
    @Override
    public DepositAmountResponse depositAmount(Long accountNumber, Double amount) {
        try {
            Account accountToDeposit = foundedAccount(accountNumber);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT.getMessage());
            }
            Double updatedBalance = accountToDeposit.getBalance() + amount;
            accountToDeposit.setBalance(updatedBalance);
            bankAccountRepository.save(accountToDeposit);

            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction(accountNumber, amount);

            //thread to avoid conflict
            try {
                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            transactionRepository.save(depositTransaction);
            DepositAmountResponse depositAmountResponse = new DepositAmountResponse(true,
                    amount + ForAccount.DEPOSIT_AMOUNT.getMessage());
            return depositAmountResponse;

        } catch (DeactivateAccountException exception) {
            exception.printStackTrace();
            throw new DeactivateAccountException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    //withdraw amount from given account number
    @Override
    public WithdrawAmountResponse withdrawAmount(Long accountNumber, Double amount) {

        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = foundedAccount(accountNumber);

            //checking account status
            if (accountWithdraw.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT.getMessage());
            }

            //if the amount for withdraw is more than balance then throws exception
            if (accountWithdraw.getBalance() < amount) {
                throw new InsufficientBalanceException(ErrorsForAccount.INSUFFICIENT_BALANCE.getMessage());
            }
            Double updatedBalance = accountWithdraw.getBalance() - amount;
            accountWithdraw.setBalance(updatedBalance);

            //updating balance into repository
            bankAccountRepository.save(accountWithdraw);

            //saving this transaction of amount into transaction repository
            BankTransaction depositTransaction = new BankTransaction(amount, accountNumber);

            //update transaction into transaction repository
            transactionRepository.save(depositTransaction);
            WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse(true,
                    amount + ForAccount.WITHDRAW_AMOUNT.getMessage());

            //thread to avoid conflict
            try {
                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return withdrawAmountResponse;

        } catch (DeactivateAccountException exception) {
            exception.printStackTrace();
            throw new DeactivateAccountException(exception.getMessage());
        } catch (InsufficientBalanceException exception) {
            exception.printStackTrace();
            throw new InsufficientBalanceException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    //transfer money from one account to another account
    @Override
    public TransferAmountResponse moneyTransfer(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = foundedAccount(accountNumberWhoSendMoney);

            if (accountWithdraw.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT.getMessage());
            }
            if (accountWithdraw.getBalance() < amountForTransfer) {
                throw new InsufficientBalanceException(ErrorsForAccount.INSUFFICIENT_BALANCE.getMessage());
            }
            Double updatedBalance = accountWithdraw.getBalance() - amountForTransfer;
            accountWithdraw.setBalance(updatedBalance);
            bankAccountRepository.save(accountWithdraw);

            //thread to avoid conflict
            try {
                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            //JPA method by derived Query to get account by account number
            Account accountToDeposit = foundedAccount(accountNumberWhoReceiveMoney);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.INACTIVE_AMOUNT_RECEIVER_ACCOUNT.getMessage());
            }

            Double updatedBalanceInDeposit = accountToDeposit.getBalance() + amountForTransfer;
            accountToDeposit.setBalance(updatedBalanceInDeposit);
            bankAccountRepository.save(accountToDeposit);

            //thread to avoid conflict
            try {
                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction(accountNumberWhoSendMoney, accountNumberWhoReceiveMoney, amountForTransfer);
            transactionRepository.save(depositTransaction);

            TransferAmountResponse transferAmountResponse = new TransferAmountResponse(true,
                    amountForTransfer + ForAccount.TRANSFER_AMOUNT.getMessage() + updatedBalance);
            return transferAmountResponse;
        } catch (DeactivateAccountException exception) {
            exception.printStackTrace();
            throw new DeactivateAccountException(exception.getMessage());
        } catch (InsufficientBalanceException exception) {
            exception.printStackTrace();
            throw new InsufficientBalanceException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    //get account balance by account number
    @Override
    public BalanceInquiryResponse getAccountBalance(Long accountNumber) {
        try {
            Account accountAvailable = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (accountAvailable == null) {
                throw new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE);
            }
            Double availableBalance = bankAccountRepository.findByAccountNumberEquals(accountNumber).getBalance();
            BalanceInquiryResponse balanceInquiryResponse = new BalanceInquiryResponse(true,
                    ForAccount.AVAILABLE_BALANCE.getMessage() + availableBalance);
            return balanceInquiryResponse;
        } catch (AccountNotAvailableException exception) {
            throw new AccountNotAvailableException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }

    }

    //get list of transaction y account number
    //by native query fetch the transaction of particular account
    @Override
    public TransactionStatementResponse transactionsOfAccount(Long AccountNumber) {

        List<TransactionDto> transactionDtos = new ArrayList<>();

        //set values in transactionDto to get proper formatted output of transaction
        transactionRepository.findByToAccountNumberEquals(AccountNumber).stream().forEach(transaction -> {
            TransactionDto transactionDto = new TransactionToTransactionDto().entityToDto(transaction);
            transactionDtos.add(transactionDto);
        });
        TransactionStatementResponse transactionStatementResponse = new TransactionStatementResponse(true, transactionDtos);
        return transactionStatementResponse;
    }

    @Override
    public DeactivateAccountResponse deactivateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new AccountAlreadyDeactivatedException(ErrorsForAccount.ACCOUNT_ALREADY_DEACTIVATE.getMessage());
            }
            accountToDeactivate.setStatus(AccountStatus.DEACTIVATED.name());
            bankAccountRepository.save(accountToDeactivate);
            DeactivateAccountResponse deactivateAccountResponse = new DeactivateAccountResponse(true, ForAccount.DEACTIVATE_ACCOUNT.getMessage());
            return deactivateAccountResponse;
        } catch (AccountAlreadyDeactivatedException exception) {
            throw new AccountAlreadyDeactivatedException(exception.getMessage());
        }
    }

    //deactivate account
    @Override
    public ActivateAccountResponse activateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase(AccountStatus.ACTIVATED.name())) {
                throw new AccountAlreadyActivatedException(ErrorsForAccount.ACCOUNT_ALREADY_ACTIVATE.getMessage());
            }
            accountToDeactivate.setStatus(AccountStatus.ACTIVATED.name());
            bankAccountRepository.save(accountToDeactivate);

            ActivateAccountResponse activateAccountResponse = new ActivateAccountResponse(true, ForAccount.ACTIVATED_ACCOUNT.getMessage());
            return activateAccountResponse;
        } catch (AccountAlreadyActivatedException exception) {
            exception.printStackTrace();
            throw new AccountAlreadyActivatedException(exception.getMessage());
        }
    }

    @Override
    public DeactivatedAccountsResponse getAllDeactivatedAccounts() {
        try {
            List<Account> deactivatedAccounts = bankAccountRepository.findDeactivatedAccounts();
            if (deactivatedAccounts.isEmpty()) {
                throw new AccountNotAvailableException(ErrorsForAccount.NO_ANY_DEACTIVATED_ACCOUNT_FOUND.getMessage());
            }
            DeactivatedAccountsResponse deactivatedAccountsResponse = new DeactivatedAccountsResponse(true, ForAccount.LIST_OF_DEACTIVATED_ACCOUNTS.getMessage(), deactivatedAccounts);
            return deactivatedAccountsResponse;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    public Account foundedAccount(Long accountNumber) {
        try {
            Account foundedAccount = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (foundedAccount == null) {
                throw new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE.getMessage());
            }
            return foundedAccount;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }


}
