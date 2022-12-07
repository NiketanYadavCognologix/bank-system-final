package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.*;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyActivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyDeactivatedException;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerNotFoundException;
import com.cognologix.bankingApplication.exceptions.DeactivateAccountException;
import com.cognologix.bankingApplication.exceptions.IllegalTypeOfAccountException;
import com.cognologix.bankingApplication.exceptions.InsufficientBalanceException;
import com.cognologix.bankingApplication.services.BankOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//service class for banking operations
@Service
public class BankOperationServiceImplementation implements BankOperationsService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //creating and saving account into database by JPA
    @Override
    public CreatedAccountResponse createAccount(AccountDto accountDto) {
        //new account
        try {
            Account accountToSave = new Account();

            accountToSave.setAccountId(accountDto.getAccountID());
            accountToSave.setStatus("Active");
            accountToSave.setBalance(accountDto.getBalance());
            accountToSave.setAccountNumber(generateAccountNumber());

            //check the account type is proper
            String accountTypeGivenByCustomer = accountDto.getAccountType();
            if (accountTypeGivenByCustomer.equalsIgnoreCase("Savings") || accountTypeGivenByCustomer.equalsIgnoreCase("Current")) {
                accountToSave.setAccountType(accountTypeGivenByCustomer);
            } else {
                throw new IllegalTypeOfAccountException("PLease enter valid account type...");
            }

            //adding information from AccountDTO in account
            Customer customer = customerRepository.findByCustomerIdEquals(accountDto.getCustomerId());
            accountToSave.setCustomer(customer);

            //check the type account for given customer is already available or not
            List<Account> matchingAccount = bankAccountRepository.findAll().stream()
                    .filter(account -> account.getCustomer().getCustomerId() == accountDto.getCustomerId())
                    .filter(account -> account.getAccountType().equalsIgnoreCase(accountDto.getAccountType()))
                    .collect(Collectors.toList());

            //the matching account will be found then throws exception
            if (matchingAccount.isEmpty()) {
                Account account = bankAccountRepository.save(accountToSave);

                CreateAccountResponseDto createAccountResponseDto=new CreateAccountResponseDto();
                createAccountResponseDto.setCustomerName(account.getCustomer().getCustomerName());
                createAccountResponseDto.setAccountNumber(account.getAccountNumber());
                createAccountResponseDto.setAccountType(account.getAccountType());
                createAccountResponseDto.setStatus(account.getStatus());
                createAccountResponseDto.setBalance(account.getBalance());
                
                //proper response after creating new account
                CreatedAccountResponse createdAccountResponse = new CreatedAccountResponse(true,"Account created successfully...",createAccountResponseDto);

                //return the custom response
                return createdAccountResponse;
            } else {
                throw new AccountAlreadyExistException("This type of account for the customer is already available...");
            }
        } catch (IllegalTypeOfAccountException exception) {
            exception.printStackTrace();
            throw new IllegalTypeOfAccountException(exception.getMessage());
        } catch (AccountAlreadyExistException exception) {
            exception.printStackTrace();
            throw new AccountAlreadyExistException(exception.getMessage());
        } catch (CustomerNotFoundException exception) {
            exception.printStackTrace();
            throw new CustomerNotFoundException(exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    //auto generating account number for every new account
    public Long generateAccountNumber() {
//        Random random = new Random();
//        String accountNumberInString = String.valueOf(Math.round(random.nextFloat() * Math.pow(10, 12)));
        Long accountNumber = Long.valueOf(1000 + bankAccountRepository.findAll().size());
//        Long accountNumber = Long.parseLong(accountNumberInString);

        //thread to avoid conflict
        try {
            Thread.sleep(100);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return accountNumber;
    }

    //get account by account number
    @Override
    public Account getAccountByAccountNumber(Long accountNumber) {
        try {
            Account account = foundedAccount(accountNumber);
            if (account == null) {
                throw new AccountNotAvailableException("Account for given Id is not available...");
            }
            return account;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }

    //deposit amount in given account number
    @Override
    public DepositAmountResponse deposit(Long accountNumber, Double amount) {
        try {
            Account accountToDeposit = foundedAccount(accountNumber);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }
            Double updatedBalance = accountToDeposit.getBalance() + amount;
            accountToDeposit.setBalance(updatedBalance);
            bankAccountRepository.save(accountToDeposit);

            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setToAccountNumber(accountNumber);
            depositTransaction.setAmount(amount);
            depositTransaction.setOperation("Deposited...");
            depositTransaction.setDateOfTransaction(LocalDateTime.now());

            //thread to avoid conflict
            try {
                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            transactionRepository.save(depositTransaction);
            DepositAmountResponse depositAmountResponse = new DepositAmountResponse(true,
                    amount + " deposited successfully... \nAvailable balance is : " + updatedBalance);
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
    public WithdrawAmountResponse withdraw(Long accountNumber, Double amount) {

        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = foundedAccount(accountNumber);

            //checking account status
            if (accountWithdraw.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }

            //if the amount for withdraw is more than balance then throws exception
            if (accountWithdraw.getBalance() < amount) {
                throw new InsufficientBalanceException("Sorry!!! insufficient balance in your account...");
            }
            Double updatedBalance = accountWithdraw.getBalance() - amount;
            accountWithdraw.setBalance(updatedBalance);

            //updating balance into repository
            bankAccountRepository.save(accountWithdraw);

            //saving this transaction of amount into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setFromAccountNumber(accountNumber);
            depositTransaction.setAmount(amount);
            depositTransaction.setOperation("Withdraw...");
            depositTransaction.setDateOfTransaction(LocalDateTime.now());

            //update transaction into transaction repository
            transactionRepository.save(depositTransaction);
            WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse(true,
                    amount + " withdraw successfully... \nAvailable balance is : " + updatedBalance);

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
        }
    }

    //transfer money from one account to another account
    @Override
    public TransferAmountResponse moneyTransfer(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = foundedAccount(accountNumberWhoSendMoney);

            if (accountWithdraw.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Failed to transfer, your account is deactivated... \nPlease visit your bank branch...");
            }
            if (accountWithdraw.getBalance() < amountForTransfer) {
                throw new InsufficientBalanceException("Sorry!!! insufficient balance in your account...");
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
            if (accountToDeposit.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Failed to transfer, receiver account is deactivated... ");
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
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setFromAccountNumber(accountNumberWhoSendMoney);
            depositTransaction.setToAccountNumber(accountNumberWhoReceiveMoney);
            depositTransaction.setAmount(amountForTransfer);
            depositTransaction.setOperation("Transferring from " + accountNumberWhoSendMoney + " to " + accountNumberWhoReceiveMoney);
            depositTransaction.setDateOfTransaction(LocalDateTime.now());
            transactionRepository.save(depositTransaction);

            TransferAmountResponse transferAmountResponse=new TransferAmountResponse(true,
                    "Amount " + amountForTransfer + " transferred successfully... \nRemaining balance is "+updatedBalance);
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

    //get list of transaction by account number
    //by native query fetch the transaction of particular account
    @Override
    public TransactionStatementResponse transactionsOfAccount(Long AccountNumber) {

        List<TransactionDto> transactionDtos = new ArrayList<>();

        //set values in transactionDto to get proper formatted output of transaction
        transactionRepository.findByToAccountNumberEquals(AccountNumber).stream().forEach(transaction -> {
            TransactionDto transactionDto = new TransactionDto();

            transactionDto.setTransactionId(transaction.getTransactionId());
            transactionDto.setOperation(transaction.getOperation());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setDateOfTransaction(transaction.getDateOfTransaction());

            transactionDtos.add(transactionDto);
        });
        TransactionStatementResponse transactionStatementResponse=new TransactionStatementResponse(true,transactionDtos);
        return transactionStatementResponse;
    }

    @Override
    public DeactivateAccountResponse deactivateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase("deactivated")) {
                throw new AccountAlreadyDeactivatedException("Your account is already deactivated...");
            }
            accountToDeactivate.setStatus("deactivated");
            bankAccountRepository.save(accountToDeactivate);
            DeactivateAccountResponse deactivateAccountResponse = new DeactivateAccountResponse(true,"Successfully deactivated " + accountNumber + " number account");
            return deactivateAccountResponse;
        } catch (AccountAlreadyDeactivatedException exception) {
            throw new AccountAlreadyDeactivatedException(exception.getMessage());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //deactivate account
    @Override
    public ActivateAccountResponse activateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase("Active")) {
                throw new AccountAlreadyActivatedException("Your account is already Activated...");
            }
            accountToDeactivate.setStatus("Active");
            bankAccountRepository.save(accountToDeactivate);

            ActivateAccountResponse activateAccountResponse = new ActivateAccountResponse(true,"Successfully activated " + accountNumber + " number account");
            return activateAccountResponse;
        } catch (AccountAlreadyActivatedException exception) {
            exception.printStackTrace();
            throw new AccountAlreadyActivatedException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public DeactivatedAccountsResponse getAllDeactivatedAccounts() {
        try {
            List<Account> deactivatedAccounts = bankAccountRepository.findDeactivatedAccounts();
            if (deactivatedAccounts.isEmpty()) {
                throw new AccountNotAvailableException("No deactivated Account found...");
            }
            DeactivatedAccountsResponse deactivatedAccountsResponse = new DeactivatedAccountsResponse(true,"Deactivated accounts are...",deactivatedAccounts);
            return deactivatedAccountsResponse;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }

    public Account foundedAccount(Long accountNumber) {
        try {
            Account foundedAccount = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (foundedAccount == null) {
                throw new AccountNotAvailableException("Given account numbers account is not exist...");
            }
            return foundedAccount;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }


}
