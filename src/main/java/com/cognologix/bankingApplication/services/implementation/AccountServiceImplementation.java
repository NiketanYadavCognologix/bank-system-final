package com.cognologix.bankingApplication.services.implementation;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.BankBranchRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.Responses.CustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.dto.Responses.accountOperations.*;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.dto.dtoToEntity.AccountDtoToEntity;
import com.cognologix.bankingApplication.dto.entityToDto.TransactionToTransactionDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.banks.branch.Branch;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.enums.AccountStatus;
import com.cognologix.bankingApplication.enums.AccountType;
import com.cognologix.bankingApplication.enums.BankName;
import com.cognologix.bankingApplication.enums.LogMessages;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForAccount;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForBank;
import com.cognologix.bankingApplication.enums.errorWithErrorCode.ErrorsForCustomer;
import com.cognologix.bankingApplication.enums.responseMessages.ForAccount;
import com.cognologix.bankingApplication.enums.responseMessages.ForCustomer;
import com.cognologix.bankingApplication.exceptions.*;
import com.cognologix.bankingApplication.exceptions.forBank.BankNameNotFoundException;
import com.cognologix.bankingApplication.exceptions.forBank.BranchNotAvailableException;
import com.cognologix.bankingApplication.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//service class for banking operations
@Service
public class AccountServiceImplementation implements AccountService {

    private static final Logger
            LOGGER = LogManager.getLogger(AccountServiceImplementation.class);
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    //creating and saving account into database by JPA
    @Override
    public CreatedAccountResponse createAccount(AccountDto accountDto) {
        //new account
        try {
            String accountType;

            //check the account type is proper
            try {
                String accountTypeGivenByCustomer = AccountType.valueOf(accountDto.getAccountType().toUpperCase()).toString();
                accountDto.setAccountType(accountTypeGivenByCustomer);
            } catch (Exception exception) {
                throw new IllegalTypeOfAccountException(ErrorsForAccount.ILLEGAL_TYPE_OF_ACCOUNT);
            }

            try {
                String bankName = BankName.valueOf(accountDto.getBankName().toUpperCase()).toString();
                accountDto.setBankName(bankName);
            } catch (Exception exception) {
                throw new BankNameNotFoundException(ErrorsForBank.BANK_NAME_NOT_FOUND);
            }
            Branch existingBranch = bankBranchRepository.findByBranchEquals(accountDto.getBranch());
            if(null == existingBranch ||  !existingBranch.getBank().getBankName().equals(accountDto.getBankName())){
                throw new BranchNotAvailableException(ErrorsForBank.BRANCH_NOT_FOUND);
            }
            //adding information from AccountDTO in account
            Customer customer = customerRepository.findByCustomerIdEquals(accountDto.getCustomerId());
            if (customer == null) {
                throw new CustomerNotFoundException(ErrorsForCustomer.CUSTOMER_NOT_FOUND);
            }
            String ifscCode = existingBranch.getIFSCCode();

            Account newAccount = new AccountDtoToEntity().accountDtoToAccountEntity(accountDto, customer, ifscCode);

            Account isSave = bankAccountRepository.save(newAccount);
            if(null != isSave){
                ThreadContext.put("executionStep","step-2");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }
            //proper response after creating new account
            CreatedAccountResponse createdAccountResponse = new CreatedAccountResponse(true, ForAccount.CREATE_ACCOUNT.getMessage(), newAccount);

            ThreadContext.put("executionStep","step-3");
            LOGGER.info(ForAccount.CREATE_ACCOUNT.getMessage());
            //return the custom response
            return createdAccountResponse;

        } catch (IllegalTypeOfAccountException exception) {
            throw new IllegalTypeOfAccountException(exception.getMessage());
        } catch (CustomerNotFoundException exception) {
            throw new CustomerNotFoundException(exception.getMessage());
        } catch (BankNameNotFoundException exception) {
            throw new BankNameNotFoundException(exception.getMessage());
        } catch (BranchNotAvailableException exception){
            throw new BranchNotAvailableException(exception.getMessage());
        } catch(Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    //deposit amount in given account number
    @Override
    public DepositAmountResponse depositAmount(Long accountNumber, Double amount) {
        try {
            Account accountToDeposit = getAccountByAccountNumber(accountNumber);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT);
            }
            Double updatedBalance = accountToDeposit.getBalance() + amount;
            accountToDeposit.setBalance(updatedBalance);
            Account isSave = bankAccountRepository.save(accountToDeposit);
            if(null != isSave){
                ThreadContext.put("executionStep","step-2");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }
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
            ThreadContext.put("executionStep","step-3");
            LOGGER.info(amount + ForAccount.DEPOSIT_AMOUNT.getMessage());
            return depositAmountResponse;

        } catch (DeactivateAccountException exception) {
            throw new DeactivateAccountException(exception.getMessage());
        }
    }

    //withdraw amount from given account number
    @Override
    public WithdrawAmountResponse withdrawAmount(Long accountNumber, Double amount) {

        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = getAccountByAccountNumber(accountNumber);

            //checking account status
            if (accountWithdraw.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT);
            }

            //if the amount for withdraw is more than balance then throws exception
            if (accountWithdraw.getBalance() < amount) {
                throw new InsufficientBalanceException(ErrorsForAccount.INSUFFICIENT_BALANCE);
            }
            Double updatedBalance = accountWithdraw.getBalance() - amount;
            accountWithdraw.setBalance(updatedBalance);

            //updating balance into repository
            Account isSave = bankAccountRepository.save(accountWithdraw);
            if(null != isSave){
                ThreadContext.put("executionStep","step-2");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }
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
            ThreadContext.put("executionStep","step-3");
            LOGGER.info(amount + ForAccount.WITHDRAW_AMOUNT.getMessage());

            return withdrawAmountResponse;

        } catch (DeactivateAccountException exception) {
            throw new DeactivateAccountException(exception.getMessage());
        } catch (InsufficientBalanceException exception) {
            throw new InsufficientBalanceException(exception.getMessage());
        }
    }

    //transfer money from one account to another account
    @Override
    public TransferAmountResponse moneyTransfer(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = getAccountByAccountNumber(accountNumberWhoSendMoney);

            if (accountWithdraw.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.DEACTIVATE_ACCOUNT);
            }
            if (accountWithdraw.getBalance() < amountForTransfer) {
                throw new InsufficientBalanceException(ErrorsForAccount.INSUFFICIENT_BALANCE);
            }
            Double updatedBalance = accountWithdraw.getBalance() - amountForTransfer;
            accountWithdraw.setBalance(updatedBalance);
            Account isSave = bankAccountRepository.save(accountWithdraw);
            if(null != isSave){
                ThreadContext.put("executionStep","step-2.1");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }

            //thread to avoid conflict
            try {
                Thread.sleep(100);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            //JPA method by derived Query to get account by account number
            Account accountToDeposit = getAccountByAccountNumber(accountNumberWhoReceiveMoney);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new DeactivateAccountException(ErrorsForAccount.INACTIVE_AMOUNT_RECEIVER_ACCOUNT);
            }

            Double updatedBalanceInDeposit = accountToDeposit.getBalance() + amountForTransfer;
            accountToDeposit.setBalance(updatedBalanceInDeposit);
            Account depositedAccount = bankAccountRepository.save(accountToDeposit);
            if(null != depositedAccount){
                ThreadContext.put("executionStep","step-2.2");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }

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

            ThreadContext.put("executionStep","step-3");
            LOGGER.info(amountForTransfer + ForAccount.TRANSFER_AMOUNT.getMessage() + updatedBalance);
            return transferAmountResponse;
        } catch (DeactivateAccountException exception) {
            throw new DeactivateAccountException(exception.getMessage());
        } catch (InsufficientBalanceException exception) {
            throw new InsufficientBalanceException(exception.getMessage());
        }
    }

    //get account balance by account number
    @Override
    public BalanceInquiryResponse getAccountBalance(Long accountNumber) {
        try {
            Account accountAvailable = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (null == accountAvailable) {
                throw new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE);
            }
            Double availableBalance = bankAccountRepository.findByAccountNumberEquals(accountNumber).getBalance();
            BalanceInquiryResponse balanceInquiryResponse = new BalanceInquiryResponse(true,
                    ForAccount.AVAILABLE_BALANCE.getMessage() + availableBalance);
            ThreadContext.put("executionStep","step-2");
            LOGGER.info(ForAccount.AVAILABLE_BALANCE.getMessage() + availableBalance);
            return balanceInquiryResponse;
        } catch (AccountNotAvailableException exception) {
            throw new AccountNotAvailableException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
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

        LOGGER.info(ForCustomer.STATEMENT.getMessage());
        return transactionStatementResponse;
    }

    @Override
    public DeactivateAccountResponse deactivateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = getAccountByAccountNumber(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase(AccountStatus.DEACTIVATED.name())) {
                throw new AccountAlreadyDeactivatedException(ErrorsForAccount.ACCOUNT_ALREADY_DEACTIVATE);
            }
            accountToDeactivate.setStatus(AccountStatus.DEACTIVATED.name());
            Account isSave = bankAccountRepository.save(accountToDeactivate);
            if(null != isSave){
                ThreadContext.put("executionStep","step-2");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }
            DeactivateAccountResponse deactivateAccountResponse = new DeactivateAccountResponse(true, ForAccount.DEACTIVATE_ACCOUNT.getMessage());
            ThreadContext.put("executionStep","step-3");
            return deactivateAccountResponse;
        } catch (AccountAlreadyDeactivatedException exception) {
            throw new AccountAlreadyDeactivatedException(exception.getMessage());
        }
    }

    //deactivate account
    @Override
    public ActivateAccountResponse activateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = getAccountByAccountNumber(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase(AccountStatus.ACTIVATED.name())) {
                throw new AccountAlreadyActivatedException(ErrorsForAccount.ACCOUNT_ALREADY_ACTIVATE);
            }
            accountToDeactivate.setStatus(AccountStatus.ACTIVATED.name());
            Account isSave = bankAccountRepository.save(accountToDeactivate);
            if(null != isSave){
                ThreadContext.put("executionStep","step-2");
                LOGGER.info(LogMessages.SUCCESSFUL_UPDATE_DATABASE.getMessage());
            }
            ActivateAccountResponse activateAccountResponse = new ActivateAccountResponse(true, ForAccount.ACTIVATED_ACCOUNT.getMessage());
            ThreadContext.put("executionStep","step-3");
            LOGGER.info(ForAccount.ACTIVATED_ACCOUNT.getMessage());
            return activateAccountResponse;
        } catch (AccountAlreadyActivatedException exception) {
            throw new AccountAlreadyActivatedException(exception.getMessage());
        }
    }

    @Override
    public DeactivatedAccountsResponse getAllDeactivatedAccounts() {
        try {
            List<Account> deactivatedAccounts = bankAccountRepository.findDeactivatedAccounts();
            if (deactivatedAccounts.isEmpty()) {
                throw new AccountNotAvailableException(ErrorsForAccount.NO_ANY_DEACTIVATED_ACCOUNT_FOUND);
            }
            DeactivatedAccountsResponse deactivatedAccountsResponse = new DeactivatedAccountsResponse(true, ForAccount.LIST_OF_DEACTIVATED_ACCOUNTS.getMessage(), deactivatedAccounts);
            return deactivatedAccountsResponse;
        } catch (AccountNotAvailableException exception) {
            throw new AccountNotAvailableException(exception.getMessage());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public Account getAccountByAccountNumber(Long accountNumber) {
        try {
            Account foundedAccount = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (foundedAccount == null) {
                throw new AccountNotAvailableException(ErrorsForAccount.ACCOUNT_NOT_AVAILABLE);
            }
            return foundedAccount;
        } catch (AccountNotAvailableException exception) {
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }


}
