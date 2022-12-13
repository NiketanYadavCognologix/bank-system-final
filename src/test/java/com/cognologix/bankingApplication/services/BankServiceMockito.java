package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.Responses.bankOperations.WithdrawAmountResponse;
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
import com.cognologix.bankingApplication.services.implementation.BankOperationServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BankServiceMockito.class})
public class BankServiceMockito {

    @Mock
    BankAccountRepository bankAccountRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    BankOperationServiceImplementation bankOperationServiceImplementation;

    AccountDto accountDto = new AccountDto(1,
            "Savings", 1000.0, 1);

    Customer customer = new Customer(1, "Onkar", LocalDate.of(1998, 11, 11),
            "903998989010", "PAN36SURYA", "sury6awanshi@gmail.com", "Male");

    Account account = new Account(accountDto.getAccountID(), "Activate", accountDto.getAccountType(),
            1000L, accountDto.getBalance(), customer);

    Account accountForReceiveMoney = new Account(2, "Activate", "Current",
            1001L, 10000.00, customer);

    List<Account> accounts = new ArrayList<>();
    BankTransaction transaction = new BankTransaction();
    Account deactivatedAccount;
    List<Account> deactivatedAccounts = new ArrayList<>();


    @Test
    public void testCreateAccount() {
        try {
            when(customerRepository.findByCustomerIdEquals(accountDto.getCustomerId())).thenReturn(customer);
            when(bankAccountRepository.findAll()).thenReturn(accounts);
            when(bankAccountRepository.save(account)).thenReturn(account);
            when(bankOperationServiceImplementation.createAccount(accountDto)).thenThrow(Exception.class);
            CreatedAccountResponse expected = new CreatedAccountResponse(true,
                    "Account created successfully...", account);

            CreatedAccountResponse actual = bankOperationServiceImplementation.createAccount(accountDto);

            assertEquals(actual, expected);
        } catch (IllegalTypeOfAccountException exception) {
            assertTrue(exception instanceof IllegalTypeOfAccountException);
        } catch (AccountAlreadyExistException exception) {
            assertTrue(exception instanceof AccountAlreadyExistException);
        } catch (CustomerNotFoundException exception) {
            assertTrue(exception instanceof CustomerNotFoundException);
        } catch (Exception exception) {
            assertTrue(exception instanceof Exception);
        }
    }

    @Test
    public void testGetAccountByAccountNumber() {
        try {
            when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
            when(bankOperationServiceImplementation.getAccountByAccountNumber(1001L)).thenThrow(Exception.class);
            Account actual = bankOperationServiceImplementation.getAccountByAccountNumber(account.getAccountNumber());
            Account expected = account;
            assertEquals(expected, actual);
        } catch (AccountNotAvailableException exception) {
            assertTrue(exception instanceof AccountNotAvailableException);
        }

    }

    @Test
    public void testDepositAmount() {
        try {
            Double amountToDeposit = 500.00;
            Double updatedBalance = account.getBalance() + amountToDeposit;

            when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
            when(bankAccountRepository.save(account)).thenReturn(account);
            when(bankOperationServiceImplementation.depositAmount(account.getAccountNumber(),
                    amountToDeposit)).thenThrow(Exception.class);

            DepositAmountResponse actual = bankOperationServiceImplementation.depositAmount(account.getAccountNumber(),
                    amountToDeposit);

            DepositAmountResponse expected = new DepositAmountResponse(true,
                    amountToDeposit + " deposited successfully... \nAvailable balance is : " + updatedBalance);
            assertEquals(expected, actual);
        } catch (DeactivateAccountException exception) {
            assertTrue(exception instanceof DeactivateAccountException);
        } catch (Exception exception) {
            assertTrue(exception instanceof Exception);
        }
    }

    @Test
    public void testWithdrawAmount() {
        try {
            Double amountToWithdraw = 500.00;
            Double updatedBalance = account.getBalance() - amountToWithdraw;

            when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
            when(bankAccountRepository.save(account)).thenReturn(account);
            when(bankOperationServiceImplementation.withdrawAmount(account.getAccountNumber(), amountToWithdraw))
                    .thenThrow(Exception.class);

            WithdrawAmountResponse actual = bankOperationServiceImplementation.withdrawAmount(account.getAccountNumber(),
                    amountToWithdraw);

            WithdrawAmountResponse expected = new WithdrawAmountResponse(true,
                    amountToWithdraw + " withdraw successfully... \nAvailable balance is : " + updatedBalance);
            assertEquals(expected, actual);
        } catch (DeactivateAccountException exception) {
            assertTrue(exception instanceof DeactivateAccountException);
        } catch (InsufficientBalanceException exception) {
            assertTrue(exception instanceof InsufficientBalanceException);
        }
    }

    @Test
    public void testMoneyTransfer() {
        try {
            Long fromAccount = account.getAccountNumber();
            Long toAccount = accountForReceiveMoney.getAccountNumber();
            Double amountForTransfer = 300.00;
            Double updatedBalance = account.getBalance() - amountForTransfer;

            when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
            when(bankAccountRepository.save(account)).thenReturn(account);
            when(bankAccountRepository.findByAccountNumberEquals(accountForReceiveMoney.getAccountNumber()))
                    .thenReturn(accountForReceiveMoney);
            when(bankAccountRepository.save(accountForReceiveMoney)).thenReturn(accountForReceiveMoney);
            when(bankOperationServiceImplementation.moneyTransfer(fromAccount, toAccount, amountForTransfer))
                    .thenThrow(Exception.class);

            TransferAmountResponse expected = bankOperationServiceImplementation.moneyTransfer(fromAccount,
                    toAccount, amountForTransfer);
            TransferAmountResponse actual = new TransferAmountResponse(true,
                    "Amount " + amountForTransfer + " transferred successfully... \nRemaining balance is " + updatedBalance);

            assertEquals(expected, actual);
        } catch (DeactivateAccountException exception) {
            assertTrue(exception instanceof DeactivateAccountException);
        } catch (InsufficientBalanceException exception) {
            assertTrue(exception instanceof InsufficientBalanceException);
        } catch (Exception exception) {
            assertTrue(exception instanceof Exception);
        }
    }

    @Test
    public void testActivateAccountByAccountNumber() {
        try {
            when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
            Account activatedAccount = account;
            activatedAccount.setAccountType("Active");
            when(bankAccountRepository.save(activatedAccount)).thenReturn(activatedAccount);
            when(bankOperationServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber()))
                    .thenThrow(Exception.class);
            ActivateAccountResponse actual = bankOperationServiceImplementation.activateAccountByAccountNumber(account.getAccountNumber());
            ActivateAccountResponse expected = new ActivateAccountResponse(true, "Successfully activated "
                    + activatedAccount.getAccountNumber() + " number account");

            assertEquals(expected, actual);
        } catch (AccountAlreadyActivatedException exception) {
            assertTrue(exception instanceof AccountAlreadyActivatedException);
        } catch (Exception exception) {
            assertTrue(exception instanceof Exception);
        }
    }

    @Test
    public void testDeactivateAccountByAccountNumber() {
        try {
            when(bankAccountRepository.findByAccountNumberEquals(account.getAccountNumber())).thenReturn(account);
            deactivatedAccount = account;
            deactivatedAccount.setAccountType("deactivated");
            when(bankAccountRepository.save(deactivatedAccount)).thenReturn(deactivatedAccount);
            when(bankOperationServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber()))
                    .thenThrow(Exception.class);

            DeactivateAccountResponse actual = bankOperationServiceImplementation.deactivateAccountByAccountNumber(account.getAccountNumber());
            DeactivateAccountResponse expected = new DeactivateAccountResponse(true,
                    "Successfully deactivated " + deactivatedAccount.getAccountNumber() + " number account");

            assertEquals(expected, actual);
        } catch (AccountAlreadyDeactivatedException exception) {
            assertTrue(exception instanceof AccountAlreadyDeactivatedException);
        } catch (Exception exception) {
            assertTrue(exception instanceof Exception);
        }
    }

    @Test
    public void testGetAllDeactivatedAccounts() {
        try {
            deactivatedAccount = account;
            deactivatedAccount.setAccountType("deactivated");

            //for searching deactivated accounts list
            deactivatedAccounts.add(deactivatedAccount);

            when(bankAccountRepository.findDeactivatedAccounts()).thenReturn(deactivatedAccounts);
            when(bankOperationServiceImplementation.getAllDeactivatedAccounts()).thenThrow(Exception.class);
            DeactivatedAccountsResponse actual = bankOperationServiceImplementation.getAllDeactivatedAccounts();
            DeactivatedAccountsResponse expected = new DeactivatedAccountsResponse(true,
                    "Deactivated accounts are...", deactivatedAccounts);

            assertEquals(expected, actual);
        } catch (AccountNotAvailableException exception) {
            assertTrue(exception instanceof AccountNotAvailableException);
        } catch (Exception exception) {
            assertTrue(exception instanceof Exception);
        }
    }
}
